package com.example.rickandmorty

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loadmoreexample.LinearRecyclerView.Items_LinearRVAdapter
import com.example.loadmoreexample.OnLoadMoreListener
import com.example.loadmoreexample.RecyclerViewLoadMoreScroll
import com.example.rickandmorty.Api.ResponseAPI
import com.example.rickandmorty.Api.Result
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var itemsCells: ArrayList<String?>
    lateinit var loadMoreItemsCells: ArrayList<String?>
    lateinit var adapterLinear: Items_LinearRVAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var body: ResponseAPI
    var tempBody: ArrayList<Result?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ///** Set the data for our ArrayList
        setItemsData()

        //** Set the adapterLinear of the RecyclerView


        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()

        //** Set the scrollListerner of the RecyclerView
        setRVScrollListener()

//
        loadData()
    }
    private fun setItemsData() {
        itemsCells = ArrayList()
        for (i in 0..40) {
            itemsCells.add("Item $i")
        }
    }

    private fun setAdapter(results: ArrayList<Result?>) {
        adapterLinear = Items_LinearRVAdapter(results.toCollection(ArrayList()))
        adapterLinear.notifyDataSetChanged()
        items.adapter = adapterLinear
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(this)
        items.layoutManager = mLayoutManager
        items.setHasFixedSize(true)
    }

    private fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                Log.d("Contoh", "onLoadMore: ")
                LoadMoreData()
            }
        })
        items.addOnScrollListener(scrollListener)
    }
//
    private fun loadData() {
        val dataServices = ServiceBuilder.buildService(DataServices::class.java)
        val requestCall = dataServices.getData()

        requestCall.enqueue(object : Callback<ResponseAPI> {
            override fun onResponse(call: Call<ResponseAPI>, response: Response<ResponseAPI>) {
                body = response.body()!!
                Log.d("Response", ""+body)
//                soalView.onDataGetSoalComplete(body)
                for (i in 0..5) {
//                    itemsCells.add("Item $i")
                    tempBody.add(body.results[i])
                }
                setAdapter(tempBody)
            }

            override fun onFailure(call: Call<ResponseAPI>, t: Throwable) {
                Log.d("Response", ""+t)
//                soalView.onDataGetSoalError(t)
            }
        })

    }


    private fun LoadMoreData() {
        var tempBody2: ArrayList<Result?> = arrayListOf()
//        setAdapter(body.results)
        //Add the Loading View

        //Create the loadMoreItemsCells Arraylist
//        tempBody2 = ArrayList()
        //Get the number of the current Items of the main Arraylist
        val start = adapterLinear.itemCount
        //Load 16 more items
        val end = start + 5
        //Use Handler if the items are loading too fast.
        //If you remove it, the data will load so fast that you can't even see the LoadingView
        Log.d("Load", "LoadMoreData: $start $end")
        Log.d("Load", "LoadMoreData: ${tempBody.size}")
        Log.d("Load", "LoadMoreData: $tempBody2")
        if (end < 20) {
            adapterLinear.addLoadingView()
            Handler().postDelayed({
                for (i in start..end) {
                    //Get data and add them to loadMoreItemsCells ArrayList
//                        loadMoreItemsCells.add("Item $i")
                    tempBody2.add(body.results[i])
                }
                //Remove the Loading View
                adapterLinear.removeLoadingView()
                //We adding the data to our main ArrayList
                adapterLinear.addData(tempBody2)
                //Change the boolean isLoading to false
                scrollListener.setLoaded()
                //Update the recyclerView in the main thread
                items.post {
                    adapterLinear.notifyDataSetChanged()
                }
            }, 3000)
        }
    }
}