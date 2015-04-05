package ca.georgiancollege.tipcalculatorapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class MainActivity extends Activity 
{
	//Constants used when saving/restoring state
	private static final String BILL_TOTAL = "BILL_TOTAL";
	private static final String CUSTOM_PERCENT = "CUSTOM_PERCENT";
	
	private double currentBillTotal; //bill amount entered by the user
	private int currentCustomPercent; //tip% set with the SeekBar
	private EditText tipTenEditText; //displays 10% tip
	private EditText totalTenEditText; // displays total with 10% tip
	private EditText tipFifteenEditText; //displays 15% tip
	private EditText totalFifteenEditText; //displays total with 15% tip
	private EditText tipTwentyEditText; //displays 20% tip
	private EditText totalTwentyEditText; //displays total with 20% tip
	private EditText totalCustomEditText; //displays total with custom tip
	private TextView	customTipTextView;
	private EditText	tipCustomEditText;
	private EditText	billEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState); //call Superclass' version
        setContentView(R.layout.main); //inflate the GUI
        
        //Check if app just started or is restoring from memory
        if (savedInstanceState == null)
        {
        	this.currentBillTotal = 0.0; //initialize the bill total as zero
        	this.currentCustomPercent = 18;  //initialize the customized tip as 18%
        } //end if
        else  //app is being restored from memory, not executed from scratch
        {
        	//initialize the bill amount to saved amount
        	this.currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL);
        	
        	//initialize the custom tip to saved tip percent
        	this.currentCustomPercent = savedInstanceState.getInt(CUSTOM_PERCENT);
        } //end else
        
        //get references to the 10%, 15%, 20% tip and total editTexts (wire up controls)
        this.tipTenEditText = (EditText) findViewById(R.id.tipTenEditText);
        this.totalTenEditText = (EditText) findViewById(R.id.totalTenEditText);
        this.tipFifteenEditText = (EditText) findViewById(R.id.TipFifteenEditText);
        this.totalFifteenEditText = (EditText) findViewById(R.id.totalFifteenEditText);
        this.tipTwentyEditText = (EditText) findViewById(R.id.tipTwentyEditText);
        this.totalTwentyEditText = (EditText) findViewById(R.id.totalTwentyEditText);
        
        // get the TextView displaying the custom tip percentage
        this.customTipTextView = (TextView) findViewById(R.id.customTipTextView);
        
        // get the custom tip and total EditTexts
        this.tipCustomEditText = (EditText) findViewById(R.id.tipCustomEditText);
        this.totalCustomEditText = (EditText) findViewById(R.id.totalCustomEditText);
        
        // get the billEditText (Event Listener)
        this.billEditText = (EditText) findViewById(R.id.billEditText);
        
        // billEditTextWatcher handles billEditText's onTextChanged event
        this.billEditText.addTextChangedListener(this.billEditTextWatcher);
        
        // get the SeekBar used to set the custom tip amount
        SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
        customSeekBar.setOnSeekBarChangeListener(this.customSeekBarListener);
    }

    // UTILITY METHODS*********************************************************************
    private void _updateStandard() //update set percentages and totals
    {
    	double tenPercentTip;
    	double tenPercentTotal;
    	double fifteenPercentTip;
    	double fifteenPercentTotal;
    	double twentyPercentTip;
    	double twentyPercentTotal;
    	
    	// calculate bill total with a 10% tip
    	tenPercentTip = this.currentBillTotal * 0.1;
    	tenPercentTotal = this.currentBillTotal + tenPercentTip;
    	//set tipTenEditText's text to tenPercentTip
    	this.tipTenEditText.setText(String.format("%.02f", tenPercentTip));
    	// calculate bill total with a 10% tip
    	this.totalTenEditText.setText(String.format("%.02f", tenPercentTotal));
    	
    	// calculate bill total with a 15% tip
    	fifteenPercentTip = this.currentBillTotal * 0.15;
    	fifteenPercentTotal = this.currentBillTotal + fifteenPercentTip;
    	//set tipFifteenEditText's text to fifteenPercentTip
    	this.tipFifteenEditText.setText(String.format("%.02f", fifteenPercentTip));
    	// calculate bill total with a 15% tip
    	this.totalFifteenEditText.setText(String.format("%.02f", fifteenPercentTotal));
    	
    	// calculate bill total with a 20% tip
    	twentyPercentTip = this.currentBillTotal * 0.2;
    	twentyPercentTotal = this.currentBillTotal + twentyPercentTip;
    	//set tipTwentyEditText's text to twentyPercentTip
    	this.tipTwentyEditText.setText(String.format("%.02f", twentyPercentTip));
    	// calculate bill total with a 20% tip
    	this.totalTwentyEditText.setText(String.format("%.02f", twentyPercentTotal));
    	
    } //end method updateStandard
 
    private void _updateCustom() // update user-customized percentage and total
    {
    	double customTipAmount;
    	double customTotalAmount;
    	
    	// set customTipTextView' text to match the position of the SeekBar
    	customTipTextView.setText(this.currentCustomPercent + "%");
    	
    	//calculate the custom tip amount
    	customTipAmount = (this.currentBillTotal * this.currentCustomPercent) * 0.01;
    	
    	//calculate the total bill, including the custom tip
    	customTotalAmount = this.currentBillTotal + customTipAmount;
    	
    	// display the tip and total bill amounts
    	this.tipCustomEditText.setText(String.format("%.02f", customTipAmount));
    	this.totalCustomEditText.setText(String.format("%.02f", customTotalAmount));
    } //end method updateCustom

    /* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putDouble(BILL_TOTAL, this.currentBillTotal);
		outState.putInt(CUSTOM_PERCENT, this.currentCustomPercent);
	} //end method onSaveIntanceState

	private OnSeekBarChangeListener customSeekBarListener = new OnSeekBarChangeListener()
	{
		// update currentCustomPercent, then call updateCustom
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			//sets currentCustomPercent to position of the SeekBar's thumb
			currentCustomPercent = seekBar.getProgress();
			_updateCustom(); //update EditTexts for custom tip and total
		} //end method onProgressChanged
		 
		@Override
		public void onStartTrackingTouch(SeekBar seekBar){	
		} //end method onStartTrackingTouch
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		} //end method onStopTrackingTouch
		
	}; // end method OnSeekBarChangeListener
	
	//event-handling object that responds to billEditText's events
	private TextWatcher billEditTextWatcher = new TextWatcher() {
		//called when user enters a number
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			// convert billEditText' text to double
			try
			{
				currentBillTotal = Double.parseDouble(s.toString());
			} // end try
			catch (NumberFormatException e)
			{
				currentBillTotal = 0.0; //default if an exception occurs
			} //end catch
			
			//update the standard and custom tip EditTexts
			_updateStandard(); //update the 10 15, 20% EditTexts
			_updateCustom(); // update the custom tip EditTexts
		} // end method onTextChanged

		@Override
		public void afterTextChanged(Editable s){	
		} //end method afterTextChanged
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after){	
		} //end method beforeTextChanged
	}; //end method billEditTextWatcher
		
}  // End of Class MainActivity
