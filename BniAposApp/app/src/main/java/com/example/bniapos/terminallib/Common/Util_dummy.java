/*
package com.example.bniapos.terminallib.Common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.cti.generic.Terminal.Common.ISuccessResponse;
import com.cti.generic.Terminal.Constant;
import com.cti.generic.Terminal.TerminalModels.TransactionRecord;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class Util {
	String TAG = "Util";
	   public static boolean isNetworkAvailable(Context context1) {
		   try{
		    ConnectivityManager connectivity = (ConnectivityManager) context1.getSystemService(Context.CONNECTIVITY_SERVICE);
		    
		    if (connectivity == null) {  		 
		      	return false;
		    } else {
		       NetworkInfo[] info = connectivity.getAllNetworkInfo();  
		       if (info != null) {  
		          for (int i = 0; i < info.length; i++) {  
		             if (info[i].getState() == NetworkInfo.State.CONNECTED) {
		                return true;
		            }
		          }
		       }
		    }
		    return false;  
		   }
			catch(Exception e ){
				e.printStackTrace();
			}
		return false;
		 }   
	   
	   public static <T> T getJSONObject(String json, Class<T> type){
		   return new Gson().fromJson(json,type);
	   }
	   public static String getJsonString(Object src){
		   return new Gson().toJson(src);
	   }
	   
	   public static <T> T getJSON(String json, Class<T> type){
		   return new Gson().fromJson(json,type);
	   }

	public static String hexToAscii(String hexStr) {
		StringBuilder output = new StringBuilder("");

		for (int i = 0; i < hexStr.length(); i += 2) {
			String str = hexStr.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}

		return output.toString();
	}

	public static String hexToBCDASCII(String hexStr) {

	   	try {
			byte[] input = hexStr.getBytes(StandardCharsets.US_ASCII);
			byte[] output = new byte[hexStr.length() / 2];
			for (int currentIndex = 0; currentIndex < input.length; currentIndex += 2) {
				if (!ValidateHexChar(input[currentIndex])) {

					return null;
				}
				if (!ValidateHexChar(input[currentIndex + 1])) {
					return null;
				}
				output[currentIndex / 2] = (byte) (((input[currentIndex] < 58 ? input[currentIndex] - 48 : input[currentIndex] - 65) << 4) | (input[currentIndex + 1] < 58 ? input[currentIndex + 1] - 48 : input[currentIndex + 1] - 65));
			}
			String Ascii = new String(output, "UTF-8");
			return Ascii;
		}
		catch (Exception ex)
		{
			Log.e("Conversion Error",ex.getLocalizedMessage());
			return  null;
		}
	}
	private static boolean ValidateHexChar(byte firstByte)
	{
		if (!((firstByte >= 48 && firstByte <= 57) || (firstByte >= 65 && firstByte <= 70)))
		{
			return false;
		}
		return true;
	}
	public static String toHex(String ba) {
		StringBuilder str = new StringBuilder();
		for (char ch : ba.toCharArray()) {
			str.append(String.format("%02x", (int) ch));

		}
		return str.toString();
	}

	public static String toHex2BytesINT(int ba) {
		StringBuilder str = new StringBuilder();

		str.append(String.format("%x", ba));

		return str.toString();
	}

	public static TransparentProgressDialog mProgressDialog;

	public static void showProgressDialog(String message, Activity activity) {
        if (mProgressDialog == null) {
            mProgressDialog = new TransparentProgressDialog(activity);
            mProgressDialog.setText(message);
            mProgressDialog.show();
            // (activity, null, message, true, false);
        } else if (mProgressDialog.isShowing()) {
            mProgressDialog.setText(message);
        }
        SingeltonActivity.getInstance().setmContext(activity);
		handlerTimeout(activity);
    }
	public static void showProgressDialog(String message, Activity activity,boolean Cancelable) {
		if (mProgressDialog == null) {

			mProgressDialog = new TransparentProgressDialog(activity);
			mProgressDialog.setTitle("Please wait");
			mProgressDialog.setText(message);
			mProgressDialog.setCancelable(Cancelable);
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				public void onCancel(DialogInterface dialog) {
					((Activity) activity).onBackPressed();
				}
			});
			mProgressDialog.show();
			handlerTimeout(activity);

		}
		else if (mProgressDialog.isShowing()) {

            mProgressDialog.setText(message);
        }

	}
	public static void showProgressDialog(String message, Activity activity,DialogInterface.OnCancelListener cancelListner) {
		if (mProgressDialog == null) {

			mProgressDialog = new TransparentProgressDialog(activity);
			mProgressDialog.setTitle("Please wait");
			mProgressDialog.setText(message);
			mProgressDialog.setCancelable(true);
			mProgressDialog.setOnCancelListener(cancelListner);
			mProgressDialog.show();
			handlerTimeout(activity);

		}
		else if (mProgressDialog.isShowing()) {

			mProgressDialog.setText(message);
		}

	}
	public static void showProgressDialog(String Title, String message,Activity activity,boolean Cancelable) {
		if (mProgressDialog == null) {


			// mProgressDialog = ProgressDialog.show(TransactionActivity.this, null, message, true, false);
			mProgressDialog = new TransparentProgressDialog(activity);
			mProgressDialog.setTitle(Title);
			mProgressDialog.setText(message);
			mProgressDialog.setCancelable(true);
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// dialog dismiss without button press
					mProgressDialog = null;
					((Activity) activity).onBackPressed();
				}
			});
			mProgressDialog.show();
			handlerTimeout(activity);
		} else if (mProgressDialog.isShowing()) {
			mProgressDialog.setText(message);
		}
	}

	public static void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
    public static void ShowSystemMessageDialog(Context _context,
                                               String Header, String Footer) {
        DialogUniversalInfoUtils dialogAction = new DialogUniversalInfoUtils((Activity) _context);
        // dialogAction.showCancelButton(true);

        dialogAction.setText(Header, Footer);

        dialogAction.mDialogOKButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialogAction.dismissDialog();
            }
        });

        dialogAction.showDialog();
    }
	public static void ShowSystemMessageDialog(Context _context,
                                               String Header, String Footer, ISuccessResponse delegate) {
        DialogUniversalInfoUtils dialogAction = new DialogUniversalInfoUtils((Activity) _context);
        // dialogAction.showCancelButton(true);

        dialogAction.setText(Header, Footer);

        dialogAction.mDialogOKButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialogAction.dismissDialog();
                delegate.processFinish("Done");
            }
        });

        dialogAction.showDialog();
    }

	public static void ShowSystemMessageDialog(Context _context,
											   String Header, String Footer, boolean isShowCancelButton, ISuccessResponse delegate) {
		DialogUniversalInfoUtils dialogAction = new DialogUniversalInfoUtils((Activity) _context);
		// dialogAction.showCancelButton(true);

		dialogAction.setText(Header, Footer);

		dialogAction.showCancelButton(isShowCancelButton);

		dialogAction.mDialogOKButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dialogAction.dismissDialog();
				delegate.processFinish("Done");
			}
		});

		dialogAction.mDialogCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dialogAction.dismissDialog();
				delegate.processFailed("Cancel Click");
			}
		});

		dialogAction.showDialog();
	}
    public static void ProcessDebug(String Tag,String Content)
	{
		Log.d(Tag,Content);
	}


	public static Date CurrentDateTimeZone(Date currentDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constant.DateTimeFormat);
			SimpleDateFormat sdf_parser = new SimpleDateFormat(Constant.DateTimeFormat);
			if(Constant.TimeZone.length() > 0)
				sdf.setTimeZone(TimeZone.getTimeZone(Constant.TimeZone));
			return sdf_parser.parse(sdf.format(currentDate));
		} catch (Exception ex) {
			return new Date();
		}
	}
	public static Date DisplayedDateTimeZone(Date currentDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constant.DateTimeFormat);
			SimpleDateFormat sdf_parser = new SimpleDateFormat(Constant.DisplayedDateTimeFormat);
			if(Constant.TimeZone.length() > 0)
				sdf.setTimeZone(TimeZone.getTimeZone(Constant.TimeZone));
			return sdf_parser.parse(sdf.format(currentDate));
		} catch (Exception ex) {
			return new Date();
		}
	}
	public static String DisplayedDate_Prepaid() {
		try {
			Date currentDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(Constant.DateFormat_PrepaidTxn);

			return sdf.format(currentDate);
		} catch (Exception ex) {
			throw ex;
		}
	}
	public static String DisplayedTime_Prepaid() {
		try {
			Date currentDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(Constant.TimeFormat_PrepaidTxn);

			return sdf.format(currentDate);
		} catch (Exception ex) {
			throw ex;
		}
	}
	public static int GetResourceID(Context mContext, String resName) {
		return mContext.getResources().getIdentifier(resName, "drawable", mContext.getPackageName());
	}

	public static void CreateHeadingLine(JSONArray DynamicLines, String Heading) {
		try {
			JSONObject dynamicLine = new JSONObject();
			dynamicLine.put("B1", Heading);
			dynamicLine.put("B2", "");

			dynamicLine.put("B3", "");

			dynamicLine.put("B4", "");
			DynamicLines.put(dynamicLine);

		} catch (Exception ex) {

		}

	}
	public static void CreateValueLine(JSONArray DynamicLines, String Heading, String SubHeading, int Counter,boolean showCounter, double Amount, boolean isNegativeValue) {
		try {
			JSONObject dynamicLine = new JSONObject();
			dynamicLine.put("B1", Heading);
			dynamicLine.put("B2", SubHeading);

			if (showCounter)
				dynamicLine.put("B3", InputUtil.ProcessNumericToFixedDigits(3, String.valueOf(Counter)));
			else
				dynamicLine.put("B3", "");

			if (isNegativeValue)
				Amount = -Amount;
			dynamicLine.put("B4", InputUtil.ReturnIndonesianRuppee(Amount, true,isNegativeValue));
			DynamicLines.put(dynamicLine);

		} catch (Exception ex) {

		}

	}

	public static void CreatePrintLineForCardType(JSONArray DynamicLines, ArrayList<String> TAGs,
												  ArrayList<String> TxnTypes,
												  ArrayList<String> NotValidRefTxnTypes,
												  List<TransactionRecord> AllRecords,String MasterTAG, String PrintTAG,
														boolean isPrintBlankRecord,boolean isAddCounter,
														boolean isNegativeValue) {

		try {
			int Counter = 0;
			double Total = 0.0;
			for (TransactionRecord record : AllRecords) {
				if (InputUtil.CheckInStringArray(TxnTypes, record.getTxnTypeId())) {
					if(NotValidRefTxnTypes == null || (!InputUtil.CheckInStringArray(NotValidRefTxnTypes, record.getRef_TxnTypeId()))) {
						if (InputUtil.CheckAllInStringArray(TAGs, record.getCardType())) {
							Counter++;
							Total += record.getAmount();
						}
					}

				}
			}
			if(MasterTAG.length() > 0 ) {
				if (Counter > 0)
					CreateHeadingLine(DynamicLines, MasterTAG);
			}
			else {
				if (Counter > 0 || isPrintBlankRecord) {

					JSONObject dynamicLine = new JSONObject();
					dynamicLine.put("B1", "");
					dynamicLine.put("B2", PrintTAG);
					if (isAddCounter)
						dynamicLine.put("B3", InputUtil.ProcessNumericToFixedDigits(3, String.valueOf(Counter)));
					else
						dynamicLine.put("B3", "");
					if (isNegativeValue)
						Total = -Total;
					dynamicLine.put("B4", InputUtil.ReturnIndonesianRuppee(Total, true, isNegativeValue));
					DynamicLines.put(dynamicLine);
				}
			}
		} catch (Exception ex) {

		}

	}
	public static void CreateDifferentSetForCardTypes(JSONArray allDynamicLines,boolean forInstallment, ArrayList<String> OtherTAGS,List<TransactionRecord> AllRecords)
	{
		String AllCardTypes = Constant.AcquiringBankCardType.split("~")[0];
		for (String cardBrandValue:AllCardTypes.split(",")) {
			String[] cardBrandSplit=cardBrandValue.split("-");
			if(cardBrandSplit.length ==2) {
				int currentDynamicLineLength = allDynamicLines.length();
				ArrayList<String> TAGS = new ArrayList<>();

				if(OtherTAGS.size() > 0)
				TAGS.addAll(OtherTAGS);

				TAGS.add(cardBrandSplit[1].toString());
				ArrayList<String> TxnTypes = new ArrayList<>();

				if(!forInstallment)
				TxnTypes.addAll(Constant.TxnTypes_ValidSALERecord);
				else
				TxnTypes.add(Constant.InstallmentTxnType);

				TxnTypes.addAll(Constant.Void_TxnTypes);

				if(!forInstallment)
					TxnTypes.add(Constant.RefundTxnType);

				ArrayList<String> NotValidTxnTypes=null;

				if(!forInstallment) {
					NotValidTxnTypes = new ArrayList<String>();
					NotValidTxnTypes.add(Constant.InstallmentTxnType);
				}

				Util.CreatePrintLineForCardType(allDynamicLines, TAGS, TxnTypes,NotValidTxnTypes,
						AllRecords,
						cardBrandSplit[1].toString().toUpperCase(),
						"", true, true, false);
				if (allDynamicLines.length() > currentDynamicLineLength) {


					if(!forInstallment) {
						TxnTypes = new ArrayList<>();
						TxnTypes.addAll(Constant.TxnTypes_ValidSALERecord);
						Util.CreatePrintLineForCardType(allDynamicLines, TAGS, TxnTypes, NotValidTxnTypes,
								AllRecords,
								"",
								"SALE", true, true, false);
					}
					else {
						TxnTypes = new ArrayList<>();
						TxnTypes.add(Constant.InstallmentTxnType);
						Util.CreatePrintLineForCardType(allDynamicLines, TAGS, TxnTypes, NotValidTxnTypes,
								AllRecords,
								"",
								"SALE", true, true, false);

					}


					TxnTypes = new ArrayList<>();
					TxnTypes.addAll(Constant.Void_TxnTypes);
					Util.CreatePrintLineForCardType(allDynamicLines, TAGS, TxnTypes, NotValidTxnTypes,
							AllRecords,
							"",
							"VOID", true, true, true);


					if (!forInstallment) {
						TxnTypes = new ArrayList<>();
						TxnTypes.add(Constant.RefundTxnType);
						Util.CreatePrintLineForCardType(allDynamicLines, TAGS, TxnTypes, NotValidTxnTypes,
								AllRecords,
								"",
								"REFUND", true, true, true);
					}

				}

			}


		}

	}

	public static void saveLogs(String className, String functionName, String outputMessage) {
		SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
		Date funcDate 		 		= new Date();
		String outputDate 		 	= formatDate.format(funcDate);

		File root = android.os.Environment.getExternalStorageDirectory();
		File dir  = new File (root.getAbsolutePath());
		dir.mkdirs();
		File file = new File(dir, outputDate + ".log");

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			Date getDate 		 = new Date();
			String strDate 		 = sdf.format(getDate);

			FileWriter fr = new FileWriter(file, true);
			BufferedWriter br = new BufferedWriter(fr);
			PrintWriter pr = new PrintWriter(br);
			pr.println("## ## ## ## " + strDate + " ## ## ## ##");
			pr.println("Class : " + className);
			pr.println("Function : " + functionName);
			pr.println("Message : " + outputMessage);
			pr.close();
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) { }
	}
	public static void handlerTimeout(Activity activity) {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if (mProgressDialog != null) {
					dismissProgressDialog();
				}
			}
		}, 60000);

	}

}
*/
