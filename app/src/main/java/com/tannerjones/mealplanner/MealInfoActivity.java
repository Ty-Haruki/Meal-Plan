package com.tannerjones.mealplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MealInfoActivity extends AppCompatActivity {

    RecyclerView nutRV;
    RecyclerView ingRV;
    Meal meal;
    MealNutAdapter mealNutAdapter;
    private ArrayList<MealPlan> mealPlans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mealPlans = new MealSave().getMealPlans(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealinfolayout);
        Intent intent = getIntent();
        meal = (Meal) intent.getSerializableExtra("MEAL");
    }

    @Override
    protected void onStart() {
        super.onStart();
        nutRV = findViewById(R.id.nutrientsRecycler);
        LinearLayoutManager linearLayoutManagerNut = new LinearLayoutManager(this);
        nutRV.setLayoutManager(linearLayoutManagerNut);
        mealNutAdapter = new MealNutAdapter();
        nutRV.setAdapter(mealNutAdapter);
        mealNutAdapter.notifyDataSetChanged();

        TextView ingredientsTextview = findViewById(R.id.ingTextview);
        ArrayList<Ingredient> ingredientsArrayList = meal.getIngredients();

        //Handle Ingredients
        for (int i = 0; i < ingredientsArrayList.size(); i++) {
            if (meal.getId() == ingredientsArrayList.get(i).getMealId()) {
                ingredientsTextview.setText(meal.getIngredients().get(i).getName());
            }
        }


        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Converting names of meals plans to a String[];
                String[] listOfMeals = new String[mealPlans.size()];
                for (int i = 0; i < mealPlans.size(); i++) {
                    listOfMeals[i] = mealPlans.get(i).getName();
                    Log.i("PLANS", mealPlans.get(i).getName());
                }
                int choice = -1;

                AlertDialog.Builder builder = new AlertDialog.Builder(MealInfoActivity.this);
                builder.setTitle(meal.getName());
                builder.setSingleChoiceItems(listOfMeals, choice, null);


                builder.setPositiveButton("Add Meal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ListView listView = ((AlertDialog) dialogInterface).getListView();
                        Object checkedItem = listView.getAdapter().getItem(listView.getCheckedItemPosition());



                        new MealSave().updateMealPlans(mealPlans, getApplicationContext());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("Cancel", "Cancelled remove");
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    class MealNutAdapter extends RecyclerView.Adapter<MealNutHolder> {

        @NonNull
        @Override
        public MealNutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            MealNutHolder mealNutHolder = new MealNutHolder(textView);
            return mealNutHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MealNutHolder holder, int position) {
            TextView view = holder.getView();
            view.setText(meal.getNutrients().get(position).getName());
        }

        @Override
        public int getItemCount() {
            return meal.getNutrients().size();
        }
    }

    class MealNutHolder extends RecyclerView.ViewHolder {

        private TextView view;

        public MealNutHolder(@NonNull TextView itemView) {
            super(itemView);
            this.view = itemView;
        }

        public TextView getView(){
            return view;
        }

    }
}
