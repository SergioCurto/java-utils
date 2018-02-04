package scurto.utils;

import java.util.Arrays;
import java.util.List;

public class StatisticUtils {
	private int tp = 0;
	private int tn = 0;
	private int fp = 0;
	private int fn = 0;
	
	public StatisticUtils(){
		this.tp = 0;
		this.fp = 0;
		this.tn = 0;
		this.fn = 0;
	}

	public StatisticUtils(int truePositives, int trueNegatives,
			int falsePositives, int falseNegatives) {
		super();
		this.tp = truePositives;
		this.tn = trueNegatives;
		this.fp = falsePositives;
		this.fn = falseNegatives;
	}
	
	public void appendStatistics(StatisticUtils otherStats){
		this.tp += otherStats.tp;
		this.tn += otherStats.tn;
		this.fp += otherStats.fp;
		this.fn += otherStats.fn;
	}

	public Double getPrecision(){
		return StatisticUtils.precision(tp, fp);
	}
	public Double getRecall(){
		return StatisticUtils.recall(tp, fn);
	}
	public Double getAccuracy(){
		return StatisticUtils.accuracy(tp, tn, fp, fn);
	}
	public Double getF1Measure(){
		return StatisticUtils.f1Measure(tp, tn, fp, fn);
	}
	public Double getF0_5Measure(){
		return StatisticUtils.f0_5Measure(tp, tn, fp, fn);
	}
	public Double getF2Measure(){
		return StatisticUtils.f2Measure(tp, tn, fp, fn);
	}
	public Double getTruePositiveRate(){
		return StatisticUtils.truePositiveRate(tp, fn);
	}
	public Double getTrueNegativeRate(){
		return StatisticUtils.trueNegativeRate(tn, fp);
	}
	public Double getMatthewsCorrelationCoefficient(){
		return StatisticUtils.matthewsCorrelationCoefficient(tp, tn, fp, fn);
	}

	public void incrementTruePositives(){
		this.tp++;
	}
	public void incrementTrueNegatives(){
		this.tn++;
	}
	public void incrementFalsePositives(){
		this.fp++;
	}
	public void incrementFalseNegatives(){
		this.fn++;
	}
	
	public int getTruePositives() {
		return tp;
	}
	public int getTrueNegatives() {
		return tn;
	}
	public int getFalsePositives() {
		return fp;
	}
	public int getFalseNegatives() {
		return fn;
	}
	
	public static List<String> generateCsvHeader(){
		final String[] headerArray = {
				"label", "label2",
				"TP", "TN", "FP", "FN",
				"Precision", "Recall", "Accuracy", "F1Measure", "F0.5Measure", "F2Measure", "TPRate", "TNRate", "MCC_MatthewsCorrelationCoefficient"
		};
		
		return Arrays.asList(headerArray);
	}
	public List<String> toCsvLine(String firstLabel, String secondLabel) {
		String[] lineArray = {
				firstLabel, secondLabel,
				String.valueOf(tp), String.valueOf(tn), String.valueOf(fp),  String.valueOf(fn),
				getPrecision().toString(), getRecall().toString(), getAccuracy().toString(), getF1Measure().toString(), getF0_5Measure().toString(), getF2Measure().toString(), getTruePositiveRate().toString(), getTrueNegativeRate().toString(), getMatthewsCorrelationCoefficient().toString()
		};
		
		return Arrays.asList(lineArray);
	}

	

	@Override
	public String toString() {
		return "[StatisticUtils; truePositives=" + tp
				+ ", trueNegatives=" + tn + ", falsePositives="
				+ fp + ", falseNegatives=" + fn
				+ ", getPrecision()=" + getPrecision() + ", getRecall()="
				+ getRecall() + ", getAccuracy()=" + getAccuracy()
				+ ", getF1Measure()=" + getF1Measure()
				+ ", getF0_5Measure()=" + getF0_5Measure()
				+ ", getF2Measure()=" + getF2Measure()
				+ ", getTruePositiveRate()=" + getTruePositiveRate()
				+ ", getTrueNegativeRate()=" + getTrueNegativeRate()
				+ ", getMatthewsCorrelationCoefficient()="
				+ getMatthewsCorrelationCoefficient() + "]";
	}

	public static double precision(int tp, int fp){
		return (tp) / ((double) tp + fp);
	}
	public static double recall(int tp, int fn){
		return (tp) / ((double) tp + fn);
	}
	public static double accuracy(int tp, int tn, int fp, int fn){
		return (tp+tn) / ((double) tp + tn + fp + fn);
	}
	/**
	 * F-Measure with balance between recall and precision (beta = 1)
	 */
	public static double f1Measure(int tp, int tn, int fp, int fn){
		return fMeasure(1, tp, tn, fp, fn);
	}
	/**
	 * F-Measure with emphasis on precision over recall (beta = 0.5)
	 */
	public static double f0_5Measure(int tp, int tn, int fp, int fn){
		return fMeasure(0.5, tp, tn, fp, fn);
	}
	/**
	 * F-Measure with emphasis on recall over precision (beta = 2.0)
	 */
	public static double f2Measure(int tp, int tn, int fp, int fn){
		return fMeasure(2, tp, tn, fp, fn);
	}
	public static double fMeasure(double beta, int tp, int tn, int fp, int fn){
		return ((1+beta*beta)*tp)/((1+beta*beta)*tp+fp+fn);
	}
	public static double trueNegativeRate(int tn, int fp){
		return tn/((double)tn+fp);
	}
	public static double truePositiveRate(int tp, double fn){
		return tp/((double)tp+fn);
	}
	public static double matthewsCorrelationCoefficient(int tp, int tn, int fp, int fn){
		double divisor = ((double)tp+fp)*(tp+fn)*(tn+fp)*(tn+fn);
		if(divisor == 0) {
			// to cover the limit case where one of the groups is 0
			divisor = 1;
		}
		
		return ((tp*tn)-(fp*fn))/
				Math.sqrt(divisor);
	}
}
