package com.udacity.asteroidradar.presentation.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.mappers.map
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.model.PictureOfDayModel
import com.udacity.asteroidradar.presentation.adapters.AsteroidListAdapter
import java.lang.Exception

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    val b: FragmentMainBinding
        get() = binding ?: throw Exception("FragmentMainBinding not initialized")

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]
    }
    var asteroidOfDay: PictureOfDayModel? = null

    private val adapter = AsteroidListAdapter().apply {
        onItemClickListener = {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater)
        b.lifecycleOwner = this
        b.viewModel = viewModel
        setHasOptionsMenu(true)

        loadPictureOfDay()
        initObservers()

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.asteroidRecycler.adapter = adapter
        initListeners()
    }

    private fun initListeners() {
        b.activityMainImageOfTheDay.setOnClickListener {
            asteroidOfDay?.let { asteroid ->
                val builder = AlertDialog.Builder(requireActivity())
                    .setMessage(asteroid.explanation)
                    .setPositiveButton(android.R.string.ok, null)
                builder.create().show()
            }
        }
    }

    private fun initObservers() {
        viewModel.asteroidList.observe(viewLifecycleOwner) {
            adapter.submitList(it.map { asteroid -> asteroid.map() })
            b.asteroidRecycler.scrollToPosition(0)
        }
        viewModel.progressBarShowing.observe(viewLifecycleOwner) {
            b.statusLoadingWheel.isVisible = it
        }
        viewModel.pictureOfDay.observe(viewLifecycleOwner) {
            asteroidOfDay = it
            Picasso.with(context)
                .load(it.url)
                .into(b.activityMainImageOfTheDay, object : Callback {

                    override fun onSuccess() {
                        b.statusLoadingWheel.visibility = View.GONE;
                    }

                    override fun onError() {
                    }
                })
        }
    }

    private fun loadPictureOfDay() {
        b.statusLoadingWheel.visibility = View.VISIBLE
        viewModel.getPictureOfDay()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> {
                viewModel.loadWeekAsteroids()
            }
            R.id.show_rent_menu -> {
                viewModel.loadTodayAsteroids()
            }
            R.id.show_buy_menu -> {
                viewModel.loadAllSavedAsteroids()
            }
        }
        return true
    }
}
