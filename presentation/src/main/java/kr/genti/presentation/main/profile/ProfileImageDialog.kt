package kr.genti.presentation.main.profile

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kr.genti.core.base.BaseDialog
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogProfileImageBinding

class ProfileImageDialog :
    BaseDialog<DialogProfileImageBinding>(R.layout.dialog_profile_image) {
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }
}
