package com.ocwvar.mvvmplayground.view.fragment.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.ocwvar.mvvmplayground.base.BaseVMFragment
import com.ocwvar.mvvmplayground.databinding.FragmentHomeBinding
import com.ocwvar.mvvmplayground.base.RemoteRequestViewModelFactory
import com.ocwvar.mvvmplayground.base.ext.setOnClickListenerWithDelay
import com.ocwvar.mvvmplayground.view.fragment.a12.dialog.A12DialogFragment
import com.ocwvar.mvvmplayground.view.fragment.account.AccountFragment
import com.ocwvar.mvvmplayground.view.fragment.image.ImageFragment
import com.ocwvar.mvvmplayground.viewmodel.home.HomeViewModel

@SuppressLint("SetTextI18n")
class HomeFragment : BaseVMFragment<HomeViewModel>() {

    companion object {
        fun newPage(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var viewBinding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels {
        RemoteRequestViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //create view binding
        this.viewBinding = FragmentHomeBinding.inflate(LayoutInflater.from(this.context))
        return this.viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init button status first
        initButtonStatus()

        //ob server data change from live date from view model of this page
        getViewModel().liveData.observe(viewLifecycleOwner) { model ->
            //apply any change to Ui
            this.viewBinding.homeContent.text = model.content + "  ${System.currentTimeMillis()}"
        }

        // bind click event to button
        this.viewBinding.homeFetch.setOnClickListenerWithDelay {
            getViewModel().fetch()
        }

        this.viewBinding.homeImage.setOnClickListenerWithDelay {
            nextFragment(this, ImageFragment.newPage())
        }

        this.viewBinding.homeNextPage.setOnClickListenerWithDelay {
            nextFragment(this, AccountFragment.newPage())
        }

        this.viewBinding.homeA12Dialog.setOnClickListenerWithDelay {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                nextFragment(this, A12DialogFragment.newPage())
            }
        }

        this.viewBinding.homeThemeAuto.setOnClickListenerWithDelay {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        this.viewBinding.homeThemeLight.setOnClickListenerWithDelay {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        this.viewBinding.homeThemeNight.setOnClickListenerWithDelay {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    /**
     * init all buttons status
     */
    private fun initButtonStatus() {
        // day-night buttons
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                this.viewBinding.homeThemeNight.isEnabled = false
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                this.viewBinding.homeThemeLight.isEnabled = false
            }

            else -> {
                this.viewBinding.homeThemeAuto.isEnabled = false
            }
        }

        // A12 button
        this.viewBinding.homeA12Dialog.isEnabled = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
    }

    /**
     * @return current view model of this page
     */
    override fun onCreateViewModel(): HomeViewModel = this.viewModel

    /**
     * pass necessary data for setup toolbar
     *
     * @return < Fragment title, Should show back button >
     */
    override fun onGetToolbarData(): Pair<String, Boolean> {
        return Pair("Playground home", false)
    }

    /**
     * call when loading status has been changed
     *
     * @param isBeginLoading whether current is under loading
     */
    override fun onLoadingStatusChanged(isBeginLoading: Boolean) {
        this.viewBinding.homeLoadingPanel.visibility = if (isBeginLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * call when an error was occurred
     *
     * @param errorCode Error code
     */
    override fun onError(errorCode: String) {
        this.viewBinding.homeContent.text = "ERROR CODE: $errorCode"
    }
}