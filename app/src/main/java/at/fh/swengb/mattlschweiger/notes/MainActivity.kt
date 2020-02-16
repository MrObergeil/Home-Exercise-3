package at.fh.swengb.mattlschweiger.notes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*





class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val token = sharedPref.getString("ACCESS_TOKEN", null)


        if (token != null)
        {
            val intent = Intent(this, NotesList::class.java)
            startActivity(intent)
        }




        main_button_login.setOnClickListener {

            val username = main_editText_username.text.toString()

            val password = main_editText_password.text.toString()

            if (username.isNotBlank() && password.isNotBlank() ) {
                NoteRepository.login(username, password,
                    success = {
                        val sharedPreferences =
                            getSharedPreferences(packageName, Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("ACCESS_TOKEN", it.token).apply()
                        val intent = Intent(this, NotesList::class.java)
                        startActivity(intent)
                    },
                    error = {
                        val msg: String
                        when (it)
                        {
                            90 -> msg = getString(R.string.error_msg_internet)
                            else -> msg = getString(R.string.error_msg_other)
                        }
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    })
            }

            else
            {
                main_editText_password.text.clear()
                main_editText_username.text.clear()
                Toast.makeText(
                    this,
                    getString(R.string.error_msg_pw),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        main_editText_password.text.clear()
        main_editText_username.text.clear()
    }
}
