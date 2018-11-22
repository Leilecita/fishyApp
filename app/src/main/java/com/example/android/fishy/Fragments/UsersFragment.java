package com.example.android.fishy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.fishy.CurrentValuesHelper;
import com.example.android.fishy.CustomLoadingListItemCreator;
import com.example.android.fishy.Events.EventOrderState;
import com.example.android.fishy.R;
import com.example.android.fishy.activities.CreateUserActivity;
import com.example.android.fishy.adapters.UserAdapter;
import com.example.android.fishy.network.ApiClient;
import com.example.android.fishy.network.Error;
import com.example.android.fishy.network.GenericCallback;
import com.example.android.fishy.network.models.User;
import com.example.android.fishy.network.models.reportsOrder.ValuesOrderReport;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class UsersFragment extends BaseFragment implements Paginate.Callbacks{

    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View mRootView;
    private TextView pendients;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;
    private String mQuery = "";
    private String token = "";

    public void onClickButton(){ activityAddUser(); }
    public int getIconButton(){
        return R.drawable.addperson;
    }

    public int getVisibility(){
        return 0;
    }

    public String getHint() {
        return "hola";
    }

    public void onClickAction(){

    }

    @Override
    public void onRefresh() {

    }

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView=inflater.inflate(R.layout.fragment_users, container, false);

        mRecyclerView = mRootView.findViewById(R.id.list_users);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserAdapter(getActivity(), new ArrayList<User>());

        registerForContextMenu(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        setHasOptionsMenu(true);

        pendients=mRootView.findViewById(R.id.pendients);


      final SearchView searchView= mRootView.findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
         //searchView.setActivated(true);
        //searchView.onActionViewExpanded();
       searchView.setQueryHint("Buscar");
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }
           @Override
           public boolean onQueryTextChange(String newText) {
               if(!newText.trim().toLowerCase().equals(mQuery)) {
                   mCurrentPage = 0;
                   mAdapter.clear();
                   listUsers(newText.trim().toLowerCase());
               }
               return false;
           }
       });

        implementsPaginate();
        loadCantOrdersPendient();

        return mRootView;
    }

    private void loadCantOrdersPendient(){
          ApiClient.get().getTotalOrdersPendients(new GenericCallback<ValuesOrderReport>() {
            @Override
            public void onSuccess(ValuesOrderReport data) {
                pendients.setText(String.valueOf(data.pendients));
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("OnResume");
        if(!isLoading()) {
            mCurrentPage = 0;
            mAdapter.clear();
            hasMoreItems=true;
            listUsers("");
            loadCantOrdersPendient();
        }

    }

    public void listUsers(final String query){
        loadingInProgress=true;
        this.mQuery = query;
        final String newToken = UUID.randomUUID().toString();
        this.token =  newToken;
        ApiClient.get().searchUsers(query, mCurrentPage, new GenericCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if(token.equals(newToken)){
                    Log.e("TOKEN", "Llega token: " + newToken);
                    System.out.println("IMPRIME"+mCurrentPage+" data size "+data.size());
                    if (query == mQuery) {

                        if (data.size() == 0) {
                            hasMoreItems = false;
                        }else{
                            int prevSize = mAdapter.getItemCount();
                            mAdapter.pushList(data);
                            mCurrentPage++;
                            if(prevSize == 0){
                                layoutManager.scrollToPosition(0);
                            }
                        }
                        loadingInProgress = false;
                    }
                }else{
                    Log.e("TOKEN", "Descarta token: " + newToken);
                }
            }

            @Override
            public void onError(Error error) {
                loadingInProgress = false;
            }
        });
    }

    private void activityAddUser(){
        startActivity(new Intent(getContext(), CreateUserActivity.class));
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Ingrese nombre");

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.requestFocus();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.trim().toLowerCase().equals(mQuery)) {
                    mCurrentPage = 0;
                    mAdapter.clear();
                    listUsers(newText.trim().toLowerCase());
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void implementsPaginate(){

        loadingInProgress=false;
        mCurrentPage=0;
        hasMoreItems = true;

        paginate= Paginate.with(mRecyclerView, this)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator())
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return 0;
                    }
                })
                .build();
    }

    @Override
    public void onLoadMore() {
        listUsers(mQuery);
    }

    @Override
    public boolean isLoading() {
        return loadingInProgress;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !hasMoreItems;
    }


}
