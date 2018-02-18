package com.isseiaoki.epoxy

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.isseiaoki.epoxy.databinding.ActivityMainBinding
import com.isseiaoki.epoxy.ext.getUrlFromDrawableResId
import com.isseiaoki.epoxy.recyclerview.OnLoadMoreListener
import com.isseiaoki.epoxy.entity.SimpleItem
import com.isseiaoki.epoxy.recyclerview.controller.SimpleController
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private var controller: SimpleController? = null
  private var sharedViewPool = RecyclerView.RecycledViewPool()
  private val disposable = CompositeDisposable()
  private var offset = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    setupToolbar()
    setupRecyclerView()
    loadData()
  }

  override fun onDestroy() {
    super.onDestroy()
    controller = null
    disposable.dispose()
  }

  private fun setupToolbar() {
    setSupportActionBar(binding.toolbar)
    supportActionBar?.apply {
      binding.toolbar.title = getString(R.string.app_name)
    }
  }

  private fun setupRecyclerView() {
    val nonnullController = SimpleController(
        context = applicationContext,
        onBannerClicked = {
          Toast.makeText(this, "banner ${it.id} tapped", Toast.LENGTH_SHORT).show()
        },
        onItemClicked = {
          Toast.makeText(this, "item ${it.id} tapped", Toast.LENGTH_SHORT).show()
        }
    )
    controller = nonnullController
    binding.recyclerView.apply {
      clipChildren = false
      setController(nonnullController)
      setHasFixedSize(true)
      recycledViewPool = sharedViewPool
      val lm = GridLayoutManager(context, 2).apply {
        spanSizeLookup = nonnullController.spanSizeLookup
        recycleChildrenOnDetach = true
      }
      layoutManager = lm
      clipToPadding = false
      setItemSpacingDp(6)
      addOnScrollListener(object : OnLoadMoreListener(lm) {
        override fun onLoadMore() {
          loadMore()
        }
      })
    }
    binding.refreshLayout.apply {
      setOnRefreshListener {
        offset = 0
        loadData()
      }
    }
    controller?.showEmpty()
  }

  private fun loadData() {
    disposable.add(
        Single.zip(
            fetchBanners(),
            fetchItems(FETCH_LIMIT),
            BiFunction<List<SimpleItem>, List<SimpleItem>, Pair<List<SimpleItem>, List<SimpleItem>>> { banners, items ->
              Pair(banners, items)
            }
        )
            .doOnSubscribe { setRefreshing(true) }
            .doFinally { setRefreshing(false) }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                  controller?.update(
                      banners = it.first,
                      items = it.second
                  )
                  offset += it.second.size
                },
                {
                  Timber.w(it)
                }
            )
    )
  }

  private fun loadMore() {
    disposable.add(
        fetchItems(FETCH_LIMIT)
            .doOnSubscribe { binding.recyclerView.post { controller?.showFooter() } }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                  controller?.addAll(it)
                  offset += it.size
                },
                {
                  Timber.w(it)
                }
            )
    )
  }

  private fun fetchBanners(): Single<List<SimpleItem>> {
    return Single.create {
      val banners = listOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3)
          .mapIndexed { index, banner ->
            SimpleItem(index.toLong(),
                resources.getUrlFromDrawableResId(banner))
          }
      it.onSuccess(banners)
    }
  }

  private fun fetchItems(limit: Int): Single<List<SimpleItem>> {
    return Single.create {
      Thread.sleep(1000)
      val items = mutableListOf<SimpleItem>()
      (0 until limit).mapTo(items) {
        SimpleItem(
            id = (offset + it).toLong(),
            imageUrl = resources.getUrlFromDrawableResId(R.drawable.cat_1)
        )
      }
      it.onSuccess(items)
    }
  }

  private fun setRefreshing(isRefreshing: Boolean) {
    binding.refreshLayout.post {
      binding.refreshLayout.isRefreshing = isRefreshing
    }
  }

  companion object {
    private val FETCH_LIMIT = 20
  }

}
