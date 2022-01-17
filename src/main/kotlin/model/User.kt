package model

class User () {
    lateinit var name: String
    lateinit var email: String
    lateinit var password: String
    var saldo: Double = 0.0
    var limitSaldo: Double = 0.0

    constructor(
        name: String,
        email: String,
        password: String,
        saldo: Double = 0.0,
        limitSaldo: Double = 0.0
    ) : this() {
        name.replace(",","")
        email.replace(",","")
        password.replace(",","")
        name.trim()
        email.trim()
        password.trim()
        this.name = name
        this.email = email
        this.password = password
        this.saldo = saldo
        this.limitSaldo = limitSaldo
    }
}