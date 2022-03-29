package com.ocwvar.mvvmplayground.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ocwvar.mvvmplayground.R

abstract class BaseFragment<T : BaseViewModel> : Fragment(), BaseViewContract {

    private lateinit var viewModel: T

    /**
     * @return current view model of this page
     */
    abstract fun onCreateViewModel(): T

    /**
     * pass necessary data for setup toolbar
     *
     * @return < Fragment title, Should show back button >
     */
    abstract fun onGetToolbarData(): Pair<String, Boolean>

    /**
     * navigate to next fragment page
     */
    fun nextFragment(
        currentPage: Fragment,
        nextPage: Fragment
    ) {
        parentFragmentManager.beginTransaction().apply {
            this.add(R.id.fragmentContainer, nextPage, nextPage::class.java.simpleName)
            this.addToBackStack(null)
            this.hide(currentPage)
            this.show(nextPage)
            this.commit()
        }
    }

    /**
     * @return view model of this page
     */
    fun getViewModel(): T {
        return this.viewModel
    }

    /**
     * setup toolbar for fragment
     */
    private fun setupToolBar() {
        val toolbarData: Pair<String, Boolean> = onGetToolbarData()
        val hostActivity: HostActivity? = if (activity != null && activity is HostActivity) {
            activity as? HostActivity
        } else {
            null
        }

        hostActivity?.supportActionBar?.apply {
            this.title = toolbarData.first
            this.setDisplayHomeAsUpEnabled(toolbarData.second)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setupToolBar()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        this.viewModel = onCreateViewModel()

        //observer error status and will notice page when error code has been given
        this.viewModel.errorStatusLiveData.observe(this.viewLifecycleOwner) { errorCode: String ->
            this.onError(errorCode)
        }

        //observer loading status and will notice page when status changed
        this.viewModel.loadingStatusLiveData.observe(this.viewLifecycleOwner) { isLoading: Boolean ->
            this.onLoadingStatusChanged(isLoading)
        }
    }
}