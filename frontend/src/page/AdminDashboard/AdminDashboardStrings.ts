interface AdminDashboardTableStrings {
    username: string;
    balance: string;
    roles: string;
    amount: string;
    deposit: string;
    withdraw: string;
}

interface AdminDashboardStrings {
    adminDashboardLabel: string;
    usersLabel: string;
    newUserLabel: string;
    usernameLabel: string;
    passwordLabel: string;
    createUserLabel: string;

    table: AdminDashboardTableStrings;

}

export const Strings: AdminDashboardStrings = {
    adminDashboardLabel: "Admin Dashboard",
    usersLabel: "Users",
    newUserLabel: "New user",
    usernameLabel: "Username",
    passwordLabel: "Password",
    createUserLabel: "Create user",

    table: {
        username: "Username",
        balance: "Balance",
        roles: "Roles",
        amount: "Amount",
        deposit: "Deposit",
        withdraw: "Withdraw"
    }
};