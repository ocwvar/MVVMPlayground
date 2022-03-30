package com.ocwvar.mvvmplayground.view.fragment.a12.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.DialogCompat
import com.ocwvar.mvvmplayground.R
import com.ocwvar.mvvmplayground.base.BaseFragment
import com.ocwvar.mvvmplayground.base.ext.setOnClickListenerWithDelay
import com.ocwvar.mvvmplayground.databinding.FragmentA12DialogBinding

@RequiresApi(Build.VERSION_CODES.S)
class A12DialogFragment : BaseFragment() {

    companion object {
        fun newPage(): A12DialogFragment {
            return A12DialogFragment()
        }
    }

    private lateinit var viewBinding: FragmentA12DialogBinding

    private var dimWindowBackground: Float = 0.0f
    private var blurWindowBackground: Int = 0
    private var blurDialogBackground: Int = 0
    private var alphaDialogBackground: Int = 0

    private var blurDialog: AlertDialog? = null

    /**
     * pass necessary data for setup toolbar
     *
     * @return < Fragment title, Should show back button >
     */
    override fun onGetToolbarData(): Pair<String, Boolean> {
        return Pair("Android Blur Background Dialog", true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.viewBinding = FragmentA12DialogBinding.inflate(LayoutInflater.from(this.context))
        return this.viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // if flag [isCrossWindowBlurEnabled] is false, means this device not support window blur
        val supported = activity?.window?.windowManager?.isCrossWindowBlurEnabled ?: false
        if (!supported) {
            val dialog: AlertDialog =
                getAlertDialog("Not supported", "The flag \"isCrossWindowBlurEnabled\" is FALSE")
            dialog.setOnDismissListener { goBack() }
            dialog.show()
            return
        }

        // init seekbars
        initAllSeekBar()

        //
        this.viewBinding.dialogShow.setOnClickListenerWithDelay {
            blurDialog = getAlertDialog("Dialog title here", "Im Loooonnnnnggg content text! Im Loooonnnnnggg content text! Im Loooonnnnnggg content text! Im Loooonnnnnggg content text! Im Loooonnnnnggg content text! Im Loooonnnnnggg content text! ")
            blurDialog?.show()

            blurDialog?.window?.apply {
                this.setBackgroundDrawable(ColorDrawable(Color.argb(alphaDialogBackground, 126,85,200)))
                this.setBackgroundBlurRadius(blurDialogBackground)

                this.attributes.blurBehindRadius = blurWindowBackground
                this.attributes.dimAmount = dimWindowBackground
                this.windowManager.updateViewLayout(this.decorView, this.attributes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.blurDialog?.cancel()
        this.blurDialog = null
    }

    /**
     * init all seekbar
     */
    @SuppressLint("SetTextI18n")
    private fun initAllSeekBar() {
        // sync value to local var
        this.alphaDialogBackground = this.viewBinding.dialogAlphaDialogBackground.progress
        this.blurDialogBackground = this.viewBinding.dialogBlurDialogBackground.progress
        this.blurWindowBackground = this.viewBinding.dialogBlurWindowBackground.progress
        this.dimWindowBackground = this.viewBinding.dialogDim.progress / 10.0f

        // sync value to display textview
        this.viewBinding.dialogAlphaDialogBackgroundText.text = "Dialog background alpha: ${this.alphaDialogBackground}"
        this.viewBinding.dialogBlurDialogBackgroundText.text = "Dialog background blur: ${this.blurDialogBackground}"
        this.viewBinding.dialogBlurWindowBackgroundText.text = "Window background blur: ${this.blurWindowBackground}"
        this.viewBinding.dialogDimText.text = "Window background dim: ${this.dimWindowBackground}"

        // bind control event
        this.viewBinding.dialogAlphaDialogBackground.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.dialogAlphaDialogBackgroundText.text = "Dialog background alpha: $progress"
                alphaDialogBackground = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        this.viewBinding.dialogBlurDialogBackground.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.dialogBlurDialogBackgroundText.text = "Dialog background blur: $progress"
                blurDialogBackground = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        this.viewBinding.dialogBlurWindowBackground.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.dialogBlurWindowBackgroundText.text = "Window background blur: $progress"
                blurWindowBackground = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        this.viewBinding.dialogDim.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dimWindowBackground = progress / 10.0f
                viewBinding.dialogDimText.text = "Window background dim: $dimWindowBackground"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun getAlertDialog(title: String, message: String): AlertDialog {
        return AlertDialog.Builder(requireContext(), R.style.DialogStyle)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog?.dismiss() }
            .create()
    }

}