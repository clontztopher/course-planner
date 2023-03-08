package com.example.courseplanningtool.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class InstructorPickerFragment extends DialogFragment {

    public interface InstructorPickerDialogListener {
        public void onInstructorSelection(Instructor instructor);
    }

    private InstructorPickerDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<Instructor> instructors = new ArrayList<>();
        ArrayAdapter<Instructor> instructorAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        InstructorRepository instructorRepository = new InstructorRepository(getActivity().getApplication());
        Future<List<Instructor>> instructorFuture = instructorRepository.getAllInstructors();
        try {
            instructors = instructorFuture.get();
            instructorAdapter.addAll(instructors);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Assign Course Instructor");
        if (instructors.isEmpty()) {
            builder.setMessage("Please add instructors through the instructor menu before assigning courses.")
                    .setPositiveButton("Okay", ((dialogInterface, i) -> { return; }));
        } else {
            builder.setAdapter(instructorAdapter, (dialogInterface, i) -> listener.onInstructorSelection(instructorAdapter.getItem(i)));
        }


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InstructorPickerDialogListener) {
            listener = (InstructorPickerDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement InstructorPickerDialogListener.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}