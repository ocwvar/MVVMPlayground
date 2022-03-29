package com.ocwvar.mvvmplayground.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ocwvar.mvvmplayground.R
import com.ocwvar.mvvmplayground.databinding.ActivityHostBinding
import com.ocwvar.mvvmplayground.view.fragment.home.HomeFragment

class HostActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityHostBinding
    private var homeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityHostBinding.inflate(LayoutInflater.from(this))
        setContentView(this.viewBinding.root)
        initToolbar()
        initWithHomePage()
    }

    /**
     * display home page to container
     */
    private fun initWithHomePage() {
        if (this.homeFragment == null) {
            this.homeFragment = HomeFragment.newPage()
        }

        this.homeFragment?.apply fragment@{
            val fragmentTag: String = this@fragment::class.java.simpleName

            //do not setup again when there is already have a same fragment
            //because we may get into this function when activity is being re-create by
            //day-night theme switch or any Ui change
            if (supportFragmentManager.findFragmentByTag(fragmentTag) != null) return@fragment

            supportFragmentManager.beginTransaction().apply transaction@{
                this.replace(R.id.fragmentContainer, this@fragment, fragmentTag)
                this.commit()
            }
        }
    }

    /**
     * init toolbar and its icon
     */
    private fun initToolbar() {
        this.viewBinding.toolbar.apply {
            setSupportActionBar(this)
            this.setNavigationOnClickListener {
                supportFragmentManager.popBackStackImmediate()
            }
        }
    }

}