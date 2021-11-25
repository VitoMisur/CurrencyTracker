package com.vito.misur.currencytracker.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.heyalex.bottomdrawer.BottomDrawerDialog
import com.github.heyalex.bottomdrawer.BottomDrawerFragment
import com.github.heyalex.handle.PullHandleView
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.adapters.SupportedSymbolsAdapter
import com.vito.misur.currencytracker.callback.ModalCallback
import com.vito.misur.currencytracker.di.factory.WelcomeFragmentAssistedFactory
import com.vito.misur.currencytracker.view.data.CurrencyItem
import com.vito.misur.currencytracker.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.modal_bottom.*
import javax.inject.Inject

/** https://github.com/HeyAlex/BottomDrawer
 * Used instead of basic dropdown selector
 */
@AndroidEntryPoint
class ModalBottomFragment : BottomDrawerFragment(), ModalCallback {

    companion object {
        const val SUPPORTED_CURRENCIES_LIST = "news_ticker_latest"

        fun newInstance(supportedSymbols: List<CurrencyItem>): ModalBottomFragment {
            val args = Bundle().apply {
                putParcelableArrayList(SUPPORTED_CURRENCIES_LIST, ArrayList(supportedSymbols))
            }
            return ModalBottomFragment().apply {
                arguments = args
            }
        }
    }

//    private val sharedWelcomeViewModel: WelcomeViewModel by viewModels()

    @Inject
    lateinit var welcomeViewModelFactory: WelcomeFragmentAssistedFactory
    private val sharedWelcomeViewModel by lazy { welcomeViewModelFactory.create(WelcomeViewModel.ActivitySource.WELCOME) }

    private val supportedSymbols: List<CurrencyItem> by lazy {
        arguments?.getParcelableArrayList<CurrencyItem>(SUPPORTED_CURRENCIES_LIST)
            ?: throw IllegalArgumentException("Fragment has to contain list of ${CurrencyItem::class.java.simpleName} argument")
    }

    private val adapter: SupportedSymbolsAdapter by lazy {
        SupportedSymbolsAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.modal_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencySearchRecyclerView.adapter = adapter
        adapter.submitList(supportedSymbols)
    }

    override fun configureBottomDrawer(): BottomDrawerDialog {
        return BottomDrawerDialog.build(requireContext()) {
            theme = R.style.ModalBottomDrawer
            //configure handle view
            handleView = PullHandleView(context).apply {
                val widthHandle =
                    resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_width)
                val heightHandle =
                    resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_height)
                val params =
                    FrameLayout.LayoutParams(widthHandle, heightHandle, Gravity.CENTER_HORIZONTAL)

                params.topMargin =
                    resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_top_margin)

                layoutParams = params
            }
        }
    }

    override fun onModalItemClick(currencyItem: CurrencyItem) {
        sharedWelcomeViewModel.fetchMainCurrency(currencyItem.currencyId)
        dismissWithBehavior()
    }
}