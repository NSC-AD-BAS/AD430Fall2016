package northseattlecollege.ASLBuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSetRadiusFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetRadiusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetRadiusFragment extends Fragment {

    public static final String ARG_RADIUS = "radius";

    private SeekBar radiusValue;
    private TextView radiusDisplay;
    private float radius;

    private OnSetRadiusFragmentInteractionListener mListener;

    public SetRadiusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param radius Parameter 1.
     * @return A new instance of fragment SetRadiusFragment.
     */
    public static SetRadiusFragment newInstance(float radius) {
        SetRadiusFragment fragment = new SetRadiusFragment();
        Bundle args = new Bundle();
        args.putFloat(ARG_RADIUS, radius);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            radius = getArguments().getFloat(ARG_RADIUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_radius, container, false);

        radiusDisplay = (TextView)view.findViewById(R.id.text_radius_display);
        radiusDisplay.setText(String.valueOf(radius) + " miles");

        radiusValue = (SeekBar)view.findViewById(R.id.seek_bar_radius_value);
        radiusValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = (progress / 4.0f) + 0.25f;
                radiusDisplay.setText(String.valueOf(radius) + " miles");
                if(mListener != null) {
                    mListener.onFragmentInteraction(radius);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetRadiusFragmentInteractionListener) {
            mListener = (OnSetRadiusFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSetRadiusFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSetRadiusFragmentInteractionListener {
        void onFragmentInteraction(float radius);
    }
}
