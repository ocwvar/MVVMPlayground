package com.ocwvar.mvvmplayground.view.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.ocwvar.mvvmplayground.databinding.FragmentHomeBinding
import com.ocwvar.mvvmplayground.base.BaseFragment
import com.ocwvar.mvvmplayground.base.RemoteRequestViewModelFactory
import com.ocwvar.mvvmplayground.view.fragment.account.AccountFragment
import com.ocwvar.mvvmplayground.view.fragment.image.ImageFragment
import com.ocwvar.mvvmplayground.viewmodel.home.HomeViewModel

@SuppressLint("SetTextI18n")
class HomeFragment : BaseFragment<HomeViewModel>() {

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

        //observer data change from live date from view model of this page
        getViewModel().liveData.observe(viewLifecycleOwner) { model ->
            //apply any change to Ui
            this.viewBinding.homeContent.text = model.content + "  ${System.currentTimeMillis()}"
        }

        when(AppCompatDelegate.getDefaultNightMode()) {
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

        //bind click event to button
        this.viewBinding.homeFetch.setOnClickListener {
            getViewModel().fetch()
        }

        this.viewBinding.homeImage.setOnClickListener {
            nextFragment(this, ImageFragment.newPage())
        }

        this.viewBinding.homeNextPage.setOnClickListener {
            nextFragment(this, AccountFragment.newPage())
        }

        this.viewBinding.homeThemeAuto.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        this.viewBinding.homeThemeLight.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        this.viewBinding.homeThemeNight.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
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