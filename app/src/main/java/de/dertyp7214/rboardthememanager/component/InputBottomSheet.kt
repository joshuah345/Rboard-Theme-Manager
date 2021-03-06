package de.dertyp7214.rboardthememanager.component

import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import de.dertyp7214.rboardthememanager.R
import de.dertyp7214.rboardthememanager.core.dpToPx
import de.dertyp7214.rboardthememanager.core.setMargin
import de.dertyp7214.rboardthememanager.viewmodels.HomeViewModel

class InputBottomSheet(
    private val text: String = "",
    private val keyListener: View.OnKeyListener,
    private val onSubmit: (input: Editable?, bottomSheet: InputBottomSheet) -> Unit,
    private val onMenu: (input: View, bottomSheet: InputBottomSheet) -> Unit = { _, _ -> }
) :
    RoundedBottomSheetDialogFragment() {

    constructor() : this("", View.OnKeyListener { _, _, _ -> true }, { _, _ -> }, { _, _ -> }) {
        dismiss()
    }

    private var inputLayout: TextInputLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.input_bottom_sheet, container, false)

        val input = v.findViewById<CustomTextInputEditText>(R.id.editText)
        inputLayout = v.findViewById(R.id.inputLayout)

        inputLayout!!.setStartIconOnClickListener { onSubmit(input.text, this) }
        inputLayout!!.setEndIconOnClickListener { onMenu(it, this) }

        if (!text.isBlank()) input.setText(text)
        input.setImeActionLabel("Search", EditorInfo.IME_ACTION_SEARCH)
        input.setOnKeyListener(keyListener)
        input.setOnEditorActionListener { _, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH
                || i == EditorInfo.IME_ACTION_DONE
                || (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                onSubmit(input.text, this)
                true
            } else false
        }

        return v
    }

    fun setKeyBoardHeightObserver(
        lifecycleOwner: FragmentActivity,
        homeViewModel: HomeViewModel
    ): InputBottomSheet {
        homeViewModel.keyboardHeightObserver(lifecycleOwner, Observer {
            inputLayout?.setMargin(bottomMargin = (it + 5.dpToPx(lifecycleOwner).toInt()))
        })
        return this
    }
}