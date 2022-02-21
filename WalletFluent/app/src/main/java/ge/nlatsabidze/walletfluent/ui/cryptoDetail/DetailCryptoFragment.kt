package ge.nlatsabidze.walletfluent.ui.cryptoDetail

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log.d
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.BaseFragment
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.databinding.DetailCryptoFragmentBinding
import ge.nlatsabidze.walletfluent.extensions.setImage
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class DetailCryptoFragment :
    BaseFragment<DetailCryptoFragmentBinding>(DetailCryptoFragmentBinding::inflate) {


    private val cryptoViewModel: DetailCryptoViewModel by viewModels()
    private val args: DetailCryptoFragmentArgs by navArgs()

    private lateinit var currentMarketItem: MarketsItem

    private lateinit var arrayChart: ArrayList<Entry>
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineData: LineData

    override fun start() {
        currentMarketItem = args.marketItem

        ratesChartSetup()
        setInformationFromArgs()
        setValuesToLineChart()
    }

    @SuppressLint("SetTextI18n")
    private fun setInformationFromArgs() {
        binding.apply {
            tvItemSymbol.text = currentMarketItem.symbol.toString().uppercase(Locale.getDefault())
            ivCoin.setImage(currentMarketItem.image.toString())

            tvTotalVolume.text = resources.getString(R.string.DollarSign) + currentMarketItem.total_volume.toString().dropLast(3).substringAfterLast(".")

            tvItemPrice.text = resources.getString(R.string.DollarSign) + currentMarketItem.current_price.toString()

            if (currentMarketItem.price_change_percentage_24h.toString()[0] == '-') {
                tvPercentage.text = "▼" + currentMarketItem.price_change_percentage_24h.toString().drop(1).substring(0, 4)
                tvPercentage.setTextColor(Color.RED)
            } else {
                tvPercentage.text = "▲" + currentMarketItem.price_change_percentage_24h.toString().substring(0, 4)
                tvPercentage.setTextColor(Color.GREEN)
            }
            tvMarketCap.text = resources.getString(R.string.MarketCapRank) + currentMarketItem.market_cap_rank.toString()
        }
    }

    private fun ratesChartSetup() {
        binding.ratesChart.isDragEnabled = true
        binding.ratesChart.setScaleEnabled(true)
        binding.ratesChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.ratesChart.axisLeft.textColor = Color.WHITE
        binding.ratesChart.axisRight.textColor = Color.WHITE
        binding.ratesChart.xAxis.textColor = Color.WHITE
        binding.ratesChart.xAxis.labelRotationAngle = -45f
        binding.ratesChart.xAxis.labelCount = 7
        binding.ratesChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
    }

    private fun showChart(item: List<List<Double>>) {

        arrayChart = ArrayList()

        repeat(item.size) { i->
            arrayChart.add(Entry(i.toFloat(), item[i][1].toFloat()))
        }

        val dates = arrayListOf<String>()
        repeat(item.size) { i->
            val currentDate = item[i][0].toString().replace(".", "").replace("E", "0").substring(0, 10)
            val currentItemToLong = currentDate.toLong()
            val convertedCurrentDate = getDateTimeFromEpocLongOfSeconds(currentItemToLong)
            dates.add(convertedCurrentDate.substring(0, 20))
        }

        lineDataSet = LineDataSet(arrayChart, currentMarketItem.id.toString())
        lineData = LineData(lineDataSet)

        lineDataSet.fillAlpha = 110
        lineDataSet.color = Color.RED
        lineDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = Color.GRAY

        val dataSet = arrayListOf<ILineDataSet>()
        dataSet.add(lineDataSet)

        val xAxis = binding.ratesChart.xAxis
        xAxis.valueFormatter = XAxisValueFormatter(dates)

        val lineData = LineData(dataSet)
        lineData.setDrawValues(true)

        binding.ratesChart.data = lineData
        binding.ratesChart.axisRight.isEnabled = false
        binding.ratesChart.invalidate()
    }


    private class XAxisValueFormatter(private val values: List<String>) : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            return values.elementAt(value.toInt())
        }
    }

    private fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String {
        try {
            val netDate = Date(epoc * 1000)
            return netDate.toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun setValuesToLineChart() {
        viewLifecycleOwner.lifecycleScope.launch {
            cryptoViewModel.getValues(currentMarketItem.id.toString()).flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                it.data?.prices?.let { it1 -> showChart(it1) }
            }
        }
    }
}