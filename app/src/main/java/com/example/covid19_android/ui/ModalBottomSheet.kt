package com.example.covid19_android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.covid19_android.R
import com.example.covid19_android.formatNumber
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.modal_bottom_sheet.*

class ModalBottomSheet : BottomSheetDialogFragment() {

    private var listener: SeekBar.OnSeekBarChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.modal_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        minCasesSeekBar.setOnSeekBarChangeListener(listener)

        progressText.text =
            formatNumber(minCasesSeekBar.progress)
    }

    fun setSeekBarListener(listener: SeekBar.OnSeekBarChangeListener) {
        this.listener = listener
        minCasesSeekBar?.setOnSeekBarChangeListener(this.listener)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}
