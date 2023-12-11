package com.example.pt6_joel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private ArrayList<Team> teamsList;
    private LayoutInflater inflater;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TeamAdapter(Context context, ArrayList<Team> teamsList) {
        this.teamsList = teamsList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.team_list_item, parent, false);
        return new TeamViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teamsList.get(position);
        holder.teamName.setText("Team Name: " + team.getTeamName());
        holder.teamAbbreviation.setText("Team Abbreviation: " + team.getTeamAbbreviation());
    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView teamName;
        TextView teamAbbreviation;

        public TeamViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            teamName = itemView.findViewById(R.id.textViewTeamName);
            teamAbbreviation = itemView.findViewById(R.id.textViewTeamAbbreviation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
