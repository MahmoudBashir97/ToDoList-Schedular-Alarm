package com.mahmoudbashir.todoapp.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mahmoudbashir.todoapp.R
import com.mahmoudbashir.todoapp.ToDoApplication
import com.mahmoudbashir.todoapp.adapters.ToDoList_adapter
import com.mahmoudbashir.todoapp.dagger.component.DaggerToDoComponent
import com.mahmoudbashir.todoapp.dagger.component.ToDoComponent
import com.mahmoudbashir.todoapp.databinding.FragmentHomeListBinding
import com.mahmoudbashir.todoapp.ui.MainActivity
import com.mahmoudbashir.todoapp.viewmodel.TODOViewModel
import com.mahmoudbashir.todoapp.viewmodel.TodoViewModelProviderFactory
import dagger.internal.DaggerCollections
import javax.inject.Inject


class HomeList_Fragment : Fragment() {
    lateinit var homeBinding:FragmentHomeListBinding
    lateinit var adapt:ToDoList_adapter
    lateinit var viewModel : TODOViewModel

    /*@Inject
    lateinit var todoFactory:TodoViewModelProviderFactory*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding =  DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home_list,
            container,
            false
        )
        homeBinding.recTodoList.setHasFixedSize(true)
        homeBinding.floatingBtn.setOnClickListener {
            findNavController().navigate(HomeList_FragmentDirections.actionHomeListFragmentToAddDataToListFragment())
        }
        injectDagger()
        createViewModel()

        setUpRecyclerView()
        getAllDataList()

        val mIth = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val data = adapt.differ.currentList[position]
                    viewModel.deleteDataFromList(data)
                    adapt.notifyDataSetChanged()
                    Snackbar.make(homeBinding.root,"Successfully deleted your Reminder", Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo"){viewModel.insert(data)
                        adapt.notifyDataSetChanged()
                        }
                        show()
                    }
                }
            }

        ItemTouchHelper(mIth).apply {
            attachToRecyclerView(homeBinding.recTodoList)
        }
        return homeBinding.root
    }

    private fun injectDagger(){
        ToDoApplication.instance.todoComp.inject(this)

        val comp: ToDoComponent = DaggerToDoComponent.create()
        comp.inject(this)

    }
   private fun createViewModel(){
       viewModel = (activity as MainActivity).viewModel
    }
    private fun setUpRecyclerView(){
        adapt = ToDoList_adapter(this.requireContext())
        homeBinding.recTodoList.apply {
            adapter=adapt
        }
    }
    private fun getAllDataList(){
        homeBinding.isLoading = true
        viewModel.getAllList.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                homeBinding.isLoading = false
                adapt.differ.submitList(data)
            }
        })
    }
}