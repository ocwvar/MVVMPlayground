package com.ocwvar.mvvmplayground.view.fragment.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ocwvar.mvvmplayground.base.BaseVMFragment
import com.ocwvar.mvvmplayground.base.RemoteRequestViewModelFactory
import com.ocwvar.mvvmplayground.databinding.FragmentImageBinding
import com.ocwvar.mvvmplayground.viewmodel.image.ImageViewModel

class ImageFragment : BaseVMFragment<ImageViewModel>()  {

    companion object {
        fun newPage(): ImageFragment {
            return ImageFragment()
        }
    }

    private lateinit var viewBinding: FragmentImageBinding

    private val viewModel: ImageViewModel by viewModels {
        RemoteRequestViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.viewBinding = FragmentImageBinding.inflate(LayoutInflater.from(this.context))
        return this.viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel().liveData.observe(this.viewLifecycleOwner) { model ->
            this.viewBinding.imageDisplay.setImageBitmap(convertBytes2Bitmap(model.bytes))
        }

        this.viewBinding.imageFetch.setOnClickListener {
            getViewModel().fetch()
        }
    }

    private fun convertBytes2Bitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * @return current view model of this page
     */
    override fun onCreateViewModel(): ImageViewModel = this.viewModel

    /**
     * pass necessary data for setup toolbar
     *
     * @return < Fragment title, Should show back button >
     */
    override fun onGetToolbarData(): Pair<String, Boolean> {
        return Pair("Nice picture", true)
    }

    /**
     * call when loading status has been changed
     *
     * @param isBeginLoading whether current is under loading
     */
    override fun onLoadingStatusChanged(isBeginLoading: Boolean) {
        this.viewBinding.imageStatus.text = if (isBeginLoading) "LOADING..." else "FINISHED"
    }

    /**
     * call when an error was occurred
     *
     * @param errorCode Error code
     */
    override fun onError(errorCode: String) {
        this.viewBinding.imageStatus.text = errorCode
    }
}