package id.rizmaulana.covid19.ui.dailygraph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.rizmaulana.covid19.databinding.FragmentDailyListBinding
import id.rizmaulana.covid19.ui.adapter.DailyAdapter
import id.rizmaulana.covid19.ui.base.BaseFragment
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DailyDataListFragment : BaseFragment() {

    private val viewModel by sharedViewModel<DailyGraphViewModel>()

    private var _binding: FragmentDailyListBinding? = null
    private val binding get() = _binding!!
    private val dailyAdapter by lazy {
        DailyAdapter {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = dailyAdapter
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadRemoteDailyData()
        }
    }

    override fun observeChange() {
        observe(viewModel.loading, ::swipeLoading)
        observe(viewModel.dailyItemsVH) { items ->
            dailyAdapter.addAll(items)
        }
    }

    private fun swipeLoading(loading: Boolean) {
        with(binding.swipeRefresh) {
            post { isRefreshing = loading }
        }
    }

    companion object {
        fun newInstance() = DailyDataListFragment()
    }

}