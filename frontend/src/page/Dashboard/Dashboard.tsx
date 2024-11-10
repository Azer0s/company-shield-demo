import { AuthService } from '../../service/auth_service'
import { TransactionDto } from '../../domain/TransactionDto'
import { TransactionType } from '../../domain/TransactionType'

//IMPROVEMENT: this should be an i18n service
import { Strings, TransactionTypeStrings } from './DashboardStrings'
import { formatAmount } from '../../util/format'
import LogoutButton from '../../components/LogoutButton/LogoutButton'
import { useEffect, useState } from 'react'
import { UserDto } from '../../domain/UserDto'
import { UserService } from '../../service/user_service'
import { UserRole } from '../../domain/UserRole'
import { TransactionService } from '../../service/transaction_service'

interface DashboardProps {
  authService: AuthService
  userService: UserService
  transactionService: TransactionService
}

function formatTransactionType(strings: TransactionTypeStrings, type: TransactionType): string {
  switch (type) {
    case TransactionType.DEPOSIT:
      return strings.deposit
    case TransactionType.WITHDRAWAL:
      return strings.withdrawal
    case TransactionType.TRANSFER_OUTGOING:
      return strings.transferOut
    case TransactionType.TRANSFER_INCOMING:
      return strings.transferIn
    default:
      return type
  }
}

function Dashboard({ authService, userService, transactionService }: DashboardProps) {
  const [user, setUser] = useState<UserDto>(authService.getUser()!)
  const [transactions, setTransactions] = useState<TransactionDto[]>([])
  const [allUsers, setAllUsers] = useState<UserDto[]>([])

  const [toId, setToId] = useState('')
  const [amount, setAmount] = useState(0)

  async function loadTransactions() {
    const transactions = await transactionService.getTransactions()
      if (transactions instanceof Error) {
        //IMPROVEMENT: Use a better alert system
        alert('Failed to get transactions')
        return
      }
      setTransactions(transactions.transactions)
  }

  useEffect(() => {
    async function load() {
      // get current user for updated balance
      const currentUser = await userService.getCurrentUser()
      if (currentUser instanceof Error) {
        //IMPROVEMENT: Use a better alert system
        alert('Failed to get current user')
        return
      }
      setUser(currentUser.user)

      let usersRes = await userService.getAllUsers()
      if (usersRes instanceof Error) {
        //IMPROVEMENT: Use a better alert system
        alert('Failed to get users')
        return
      }

      let users = usersRes.users
        .filter(u => u.username !== user.username && !u.roles.includes(UserRole.ADMIN))
        .sort((a, b) => a.username.localeCompare(b.username))

      setAllUsers(users)
      setToId(users[0].id)

      loadTransactions()
    }
    load()
  }, [])

  async function transfer() {
    let res = await transactionService.transfer(toId, amount)
    if (!res) {
      //IMPROVEMENT: Use a better alert system
      alert('Failed to transfer')
      return
    }

    loadTransactions()
  }

  //IMPROVEMENT: Form validation could be better
  //IMPROVEMENT: Add a loading spinner
  return (
    <>
      <h1>{Strings.dashboardLabel}</h1>

      <hr />

      <h2>{Strings.welcomeBackLabel}, {user.username}</h2>
      <h2>{Strings.accountBalanceLabel}: {formatAmount(user.balance ?? 0)}</h2>
      <LogoutButton authService={authService} />

      <hr style={{marginTop: '1em'}} />

      <h2>{Strings.newTransactionLabel}</h2>
      <div className='card'>
        <select onChange={e => setToId(e.target.value)}>
          {allUsers.map((user, index) => {
            return (<option key={index} value={user.id}>{user.username}</option>)
          })}
        </select>
        <input type="number" placeholder={Strings.amountLabel} onChange={e => setAmount(Number(e.target.value))} />
        <button onClick={transfer}>{Strings.transferLabel}</button>
      </div>

      <hr />

      <h2>{Strings.transactionsLabel}</h2>
      <table>
        <thead>
          <tr>
            <th>{Strings.fromLabel}</th>
            <th>{Strings.toLabel}</th>
            <th>{Strings.amountLabel}</th>
            <th>{Strings.typeLabel}</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction, index) => {
            let amount = transaction.accountBalanceAfter - transaction.accountBalanceBefore;

            return (<tr key={index}>
              <td>{transaction.fromId == null ?  
                  Strings.naLabel : 
                  transaction.fromId == user.id ? Strings.selfLabel : allUsers.find(u => u.id == transaction.fromId)?.username}</td>
              <td>{transaction == null ? 
                  Strings.selfLabel : 
                  transaction.toId == user.id ? Strings.selfLabel : allUsers.find(u => u.id == transaction.toId)?.username}</td>
              <td style={{color: amount > 0 ? 'green' : 'red'}}>{formatAmount(amount)}</td>
              <td>{formatTransactionType(Strings.transactionTypeStrings, transaction.type)}</td>
            </tr>)
          })}
        </tbody>
      </table>
      
      <br />
    </>
  )
}

export default Dashboard
