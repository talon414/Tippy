package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 10
class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmt: EditText
    private lateinit var sbTipPercent: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmt: TextView
    private lateinit var tvTotalAmt: TextView
    private lateinit var tvTipAdj: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmt = findViewById(R.id.edtBaseAmount)
        sbTipPercent = findViewById(R.id.sbTipPercent)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmt = findViewById(R.id.tvTipAmount)
        tvTotalAmt = findViewById(R.id.tvTotalAmount)
        tvTipAdj = findViewById(R.id.tvTipAdj)

        tvTipPercentLabel.text="$INITIAL_TIP_PERCENT%"
        sbTipPercent.progress = INITIAL_TIP_PERCENT
        updateDescription(INITIAL_TIP_PERCENT)


        sbTipPercent.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG,"onProgressChange $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBaseAmt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                computeTipAndTotal()
            }
        })

    }

    private fun updateDescription(tipPercent: Int) {
        val tipDesc = when(tipPercent){
            in 0..8 -> "Poor"
            in 9..14 -> "Good"
            in 15..25 -> "Great"
            else -> "Amazing"
        }

        tvTipAdj.text = tipDesc

        //color text

        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/sbTipPercent.max,
            ContextCompat.getColor(this,R.color.worst_tip),
            ContextCompat.getColor(this,R.color.best_tip)
        ) as Int

        tvTipAdj.setTextColor(color)

    }

    private fun computeTipAndTotal() {
        if (etBaseAmt.text.isEmpty()){

            tvTipAmt.text = ""
            tvTotalAmt.text = ""

            return
        }

        val baseAmt = etBaseAmt.text.toString().toDouble()
        val tipPercent = sbTipPercent.progress

        val tipAmnt = baseAmt * tipPercent / 100
        val totalAmount = baseAmt + tipAmnt

        tvTipAmt.text = "%.2f".format(tipAmnt)
        tvTotalAmt.text = "%.2f".format(totalAmount)

    }
}