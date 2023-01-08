package com.iqbal.trashbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.iqbal.trashbank.admin.HomeAdminActivity
import com.iqbal.trashbank.app.ApiConfig
import com.iqbal.trashbank.helper.SharedPref
import com.iqbal.trashbank.login.ResponseLogin
import com.iqbal.trashbank.user.HomeUserActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var statusLogin = false
    private lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = SharedPref(this)

        btn_login.setOnClickListener {
            login()
        }

        // cek apakah sudah login sebelumnya
        if(s.getStatus()){
            val role = s.getString(s.id_role)
            if(role == 1.toString()){
                intent = Intent(this@LoginActivity, HomeAdminActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Toast.makeText(this@LoginActivity, "Selamat Datang Di aplikasi Bank Sampah,"+s.getString(s.role_name)+", "+s.getString(s.name), Toast.LENGTH_SHORT).show()
            }else{
                intent = Intent(this@LoginActivity, HomeUserActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Toast.makeText(this@LoginActivity, "Selamat Datang Di aplikasi Bank Sampah"+s.getString(s.role_name)+", "+s.getString(s.name), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login(){
        if (edt_nohp.text.isEmpty()){
            edt_nohp.error = "Kolom NoHp Tidak boleh Kosong"
            edt_nohp.requestFocus()
            return
        }
        if (edt_password.text.isEmpty()){
            edt_password.error = "Kolom Password Tidak boleh Kosong"
            edt_password.requestFocus()
            return
        }

        ApiConfig.instance.login(edt_nohp.text.toString(), edt_password.text.toString())
            .enqueue(object : Callback<ResponseLogin>
            {
                override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>)
                {
                    val respon = response.body()!!

                    if (respon.Success == 1 ){
                        // simpan session login
                        s.setStatusLogin(true)
                        s.setString(s.id_user, respon.id_user.toString())
                        s.setString(s.id_role, respon.id_role.toString())
                        s.setString(s.role_name, respon.role_name.toString())
                        s.setString(s.nohp, respon.nohp.toString())
                        s.setString(s.name, respon.nama.toString())

                        val role = s.getString(s.id_role)
                        if(role == 1.toString()){
                            intent = Intent(this@LoginActivity, HomeAdminActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            Toast.makeText(this@LoginActivity, "Selamat Datang Di aplikasi Bank Sampah,"+respon.role_name+", "+respon.nama, Toast.LENGTH_SHORT).show()
                        }else{
                            intent = Intent(this@LoginActivity, HomeUserActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            Toast.makeText(this@LoginActivity, "Selamat Datang Di aplikasi Bank Sampah"+respon.role_name+", "+respon.nama, Toast.LENGTH_SHORT).show()
                        }

                    }else {
                        Toast.makeText(this@LoginActivity, "ERROR" + respon.Message, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "ERROR:" + t.message, Toast.LENGTH_LONG).show()
                }

            })
    }
}