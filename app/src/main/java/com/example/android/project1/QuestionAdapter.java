package com.example.android.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter <QuestionAdapter.QuestionViewHolder> {

    private List <Question> questionList;



    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_question, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionViewHolder holder, int position) {

        Question question = questionList.get(position);

        holder.questionText.setText(question.getQuestion());
        holder.answerText.setText(question.getAnswer());
        holder.questionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.answerLayout.getVisibility() == View.GONE) {
                    holder.answerLayout.setVisibility(View.VISIBLE);

                }else if (holder.answerLayout.getVisibility() == View.VISIBLE){
                    holder.answerLayout.setVisibility(View.GONE);
                }
            }
        });

        holder.answerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.answerLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        protected TextView questionText, answerText;
        protected ImageView answerBtn;
        protected LinearLayout questionLayout;
        protected LinearLayout answerLayout;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            this.questionLayout = itemView.findViewById(R.id.question_layout);
            this.questionText = itemView.findViewById(R.id.question_text);
            this.answerBtn = itemView.findViewById(R.id.answer_btn);
            this.answerLayout = itemView.findViewById(R.id.answer_layout);
            this.answerText = itemView.findViewById(R.id.answer_text);
        }
    }
}
