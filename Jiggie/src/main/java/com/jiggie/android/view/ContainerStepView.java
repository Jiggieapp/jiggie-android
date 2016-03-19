package com.jiggie.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/18/2016.
 */
public class ContainerStepView extends RelativeLayout {

    @Bind(R.id.txt_step_title)
    TextView txtStepTitle;
    @Bind(R.id.lin_step)
    LinearLayout linStep;
    private Context context;
    String titleStep;
    ArrayList<String> arrStep;

    public ContainerStepView(Context context, String titleStep, ArrayList<String> arrStep) {
        super(context);
        this.context = context;
        this.titleStep = titleStep;
        this.arrStep = arrStep;

        initView();
    }

    public ContainerStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ContainerStepView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_relstep_view, this, true);
        ButterKnife.bind(this);

        txtStepTitle.setText(titleStep);
        int sizeStep = arrStep.size();
        for(int i=0;i<sizeStep;i++){
            StepView stepView;
            String text = arrStep.get(i);
            boolean showDivider;
            if(i==(sizeStep-1)){
                showDivider = false;
            }else {
                showDivider = true;
            }

            stepView = new StepView(context, String.valueOf(i+1), text, showDivider);
            linStep.addView(stepView);
        }
    }

}
