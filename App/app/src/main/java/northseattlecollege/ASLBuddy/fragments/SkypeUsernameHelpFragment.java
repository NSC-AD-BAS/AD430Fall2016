package northseattlecollege.ASLBuddy.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import northseattlecollege.ASLBuddy.R;

/**
 * Created by Sai Chang on 12/9/2016.
 */
public class SkypeUsernameHelpFragment extends Fragment {

    Fragment help;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.fragment_skype_profile_help, container, false);

        help = this;

        Button back = (Button) view.findViewById(R.id.back);
        assert back != null;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().remove(help).commit();
            }
        });

        return view;
    }
}
