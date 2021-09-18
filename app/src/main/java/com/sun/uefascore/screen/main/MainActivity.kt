package com.sun.uefascore.screen.main

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sun.uefascore.R
import com.sun.uefascore.screen.base.HomePageFragment
import com.sun.uefascore.utils.addFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var doubleBackPressed = false
    private fun setFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(HomePageFragment.newInstance(), containerLayout.id)
        setFlags()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else if (!doubleBackPressed) {
            this.doubleBackPressed = true
            Toast.makeText(
                this,
                getString(R.string.back_again_to_exit),
                Toast.LENGTH_SHORT
            ).show()
            Handler().postDelayed({ doubleBackPressed = false }, 2000)
            return
        } else {
            super.onBackPressed()
            return
        }
    }
}
