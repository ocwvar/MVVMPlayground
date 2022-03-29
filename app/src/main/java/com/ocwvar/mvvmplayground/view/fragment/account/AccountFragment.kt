package com.ocwvar.mvvmplayground.view.fragment.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.viewModels
import com.ocwvar.mvvmplayground.base.BaseFragment
import com.ocwvar.mvvmplayground.base.RemoteRequestViewModelFactory
import com.ocwvar.mvvmplayground.databinding.FragmentAccountBinding
import com.ocwvar.mvvmplayground.viewmodel.account.AccountViewModel

@SuppressLint("SetTextI18n")
class AccountFragment : BaseFragment<AccountViewModel>() {

    companion object {
        fun newPage(): AccountFragment {
            return AccountFragment()
        }
    }

    private lateinit var viewBinding: FragmentAccountBinding

    private val viewModel: AccountViewModel by viewModels {
        RemoteRequestViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.viewBinding = FragmentAccountBinding.inflate(LayoutInflater.from(this.context))
        return this.viewBinding.root
    }

    /**
     * @return current view model of this page
     */
    override fun onCreateViewModel(): AccountViewModel = this.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel().apply {
            this.liveData.observe(this@AccountFragment.viewLifecycleOwner) { model ->
                this@AccountFragment.viewBinding.apply viewBinding@{
                    this.accountName.text = model.name
                    this.accountStatus.text = model.status
                    this.accountTitle.text = model.title
                }
            }

            this.fetch()
        }
    }

    /**
     * pass necessary data for setup toolbar
     *
     * @return < Fragment title, Should show back button >
     */
    override fun onGetToolbarData(): Pair<String, Boolean> {
        return Pair("Account state", true)
    }

    /**
     * call when loading status has been changed
     *
     * @param isBeginLoading whether current is under loading
     */
    override fun onLoadingStatusChanged(isBeginLoading: Boolean) {
        this.viewBinding.accountLoadingPanel.visibility = if (isBeginLoading) {
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
        this.viewBinding.accountStatus.text = "ERROR CODE: $errorCode"
    }
}