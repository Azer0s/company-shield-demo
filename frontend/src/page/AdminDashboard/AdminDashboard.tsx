import { AuthService } from '../../service/auth_service'
import './AdminDashboard.css'

//IMPROVEMENT: this should be an i18n service
import { Strings } from './AdminDashboardStrings'
import { UserDto } from '../../domain/UserDto'
import { UserRole } from '../../domain/UserRole'
import { formatAmount } from '../../util/format'
import LogoutButton from '../../components/LogoutButton/LogoutButton'
import { UserService } from '../../service/user_service'
import { useEffect, useState } from 'react'
import { TransactionService } from '../../service/transaction_service'

interface AdminDashboardProps {
  authService: AuthService
  userService: UserService
  transactionService: TransactionService
}

function AdminDashboard({ authService, userService, transactionService }: AdminDashboardProps) {
  const [users, setUsers] = useState<UserDto[]>([])
  const [amounts, setAmounts] = useState<number[]>([])

  const [newUserUsername, setNewUserUsername] = useState('')
  const [newUserPassword, setNewUserPassword] = useState('')

  async function loadUsers() {
    const users = await userService.getAllUsers()
    if (users instanceof Error) {
      //IMPROVEMENT: Use a better alert system
      alert('Failed to get users')
      return
    }

    setUsers(users.users.sort((a, b) => a.username.localeCompare(b.username)))
  }

  useEffect(() => {
    loadUsers()
  }, [])

  async function deposit(user: UserDto, amount: number) {
    let res = await transactionService.deposit(user.id, amount)
    if (!res) {
      //IMPROVEMENT: Use a better alert system
      alert('Failed to deposit')
      return
    }

    setUsers(users.map(u => {
      if (u.id === user.id) {
        return {
          ...u,
          balance: u.balance + amount
        }
      }
      return u
    }))
  }

  async function withdraw(user: UserDto, amount: number) {
    let res = await transactionService.withdraw(user.id, amount)
    if (!res) {
      //IMPROVEMENT: Use a better alert system
      alert('Failed to withdraw')
      return
    }

    setUsers(users.map(u => {
      if (u.id === user.id) {
        return {
          ...u,
          balance: u.balance - amount
        }
      }
      return u
    }))
  }

  async function createUser() {
    if (!newUserUsername || !newUserPassword) {
      //IMPROVEMENT: Use a better alert system
      alert('Username and password are required')
      return
    }

    if (!await userService.createUser(newUserUsername, newUserPassword)) {
      //IMPROVEMENT: Use a better alert system
      alert('Failed to create user')
      return
    }

    await loadUsers()
  }

  //IMPROVEMENT: Form validation could be better
  //IMPROVEMENT: Add a loading spinner
  return (
    <>
      <h1>{Strings.adminDashboardLabel}</h1>

      <hr />

      <LogoutButton authService={authService} />

      <hr style={{ marginTop: '1em' }} />

      <h2>{Strings.newUserLabel}</h2>
      <div className='card'>
        <input type="text" placeholder={Strings.usernameLabel} onChange={e => setNewUserUsername(e.target.value)} />
        <input type="password" placeholder={Strings.passwordLabel} onChange={e => setNewUserPassword(e.target.value)} />
        <button onClick={createUser}>{Strings.createUserLabel}</button>
      </div>

      <hr />

      <h2>{Strings.usersLabel}</h2>
      <table>
        <thead>
          <tr>
            <th>{Strings.table.username}</th>
            <th>{Strings.table.balance}</th>
            <th>{Strings.table.roles}</th>
            <th>{Strings.table.amount}</th>
            <th>{Strings.table.withdraw}</th>
            <th>{Strings.table.deposit}</th>
          </tr>
        </thead>
        <tbody>
          {users.filter(user => !user.roles.includes(UserRole.ADMIN)).map((user, index) => {
            return (
              <tr key={index}>
                <td>{user.username}</td>
                <td>{formatAmount(user.balance)}</td>
                <td>[{user.roles.join(', ')}]</td>
                <td><input type="number" onChange={e => {
                  amounts[index] = parseInt(e.target.value)
                  setAmounts(amounts)
                }} style={{ textAlign: 'right' }}/></td>
                <td><button className='withdrawButton' onClick={() => withdraw(user, amounts[index])}>-</button></td>
                <td><button className='depositButton' onClick={() => deposit(user, amounts[index])}>+</button></td>
              </tr>
            )
          })}
        </tbody>
      </table>

      <br />
    </>
  )
}

export default AdminDashboard
