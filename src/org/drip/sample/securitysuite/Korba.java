
package org.drip.sample.securitysuite;

import org.drip.analytics.cashflow.CompositePeriod;
import org.drip.analytics.date.*;
import org.drip.product.creator.BondBuilder;
import org.drip.product.credit.BondComponent;
import org.drip.quant.common.FormatUtil;
import org.drip.quant.linearalgebra.Matrix;
import org.drip.service.env.EnvManager;
import org.drip.service.scenario.*;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for buy/side financial/trading model
 *  	libraries targeting analysts and developers
 *  	https://lakshmidrip.github.io/DRIP/
 *  
 *  DRIP is composed of four main libraries:
 *  
 *  - DRIP Fixed Income - https://lakshmidrip.github.io/DRIP-Fixed-Income/
 *  - DRIP Asset Allocation - https://lakshmidrip.github.io/DRIP-Asset-Allocation/
 *  - DRIP Numerical Optimizer - https://lakshmidrip.github.io/DRIP-Numerical-Optimizer/
 *  - DRIP Statistical Learning - https://lakshmidrip.github.io/DRIP-Statistical-Learning/
 * 
 *  - DRIP Fixed Income: Library for Instrument/Trading Conventions, Treasury Futures/Options,
 *  	Funding/Forward/Overnight Curves, Multi-Curve Construction/Valuation, Collateral Valuation and XVA
 *  	Metric Generation, Calibration and Hedge Attributions, Statistical Curve Construction, Bond RV
 *  	Metrics, Stochastic Evolution and Option Pricing, Interest Rate Dynamics and Option Pricing, LMM
 *  	Extensions/Calibrations/Greeks, Algorithmic Differentiation, and Asset Backed Models and Analytics.
 * 
 *  - DRIP Asset Allocation: Library for model libraries for MPT framework, Black Litterman Strategy
 *  	Incorporator, Holdings Constraint, and Transaction Costs.
 * 
 *  - DRIP Numerical Optimizer: Library for Numerical Optimization and Spline Functionality.
 * 
 *  - DRIP Statistical Learning: Library for Statistical Evaluation and Machine Learning.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * Korba generates the Full Suite of Replication Metrics for Bond Korba.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class Korba {

	public static final void main (
		final String[] astArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		JulianDate dtSpot = DateUtil.CreateFromYMD (
			2017,
			DateUtil.OCTOBER,
			5
		);

		String[] astrDepositTenor = new String[] {
			"2D"
		};

		double[] adblDepositQuote = new double[] {
			0.0130411 // 2D
		};

		double[] adblFuturesQuote = new double[] {
			0.01345,	// 98.655
			0.01470,	// 98.530
			0.01575,	// 98.425
			0.01660,	// 98.340
			0.01745,    // 98.255
			0.01845     // 98.155
		};

		String[] astrFixFloatTenor = new String[] {
			"02Y",
			"03Y",
			"04Y",
			"05Y",
			"06Y",
			"07Y",
			"08Y",
			"09Y",
			"10Y",
			"11Y",
			"12Y",
			"15Y",
			"20Y",
			"25Y",
			"30Y",
			"40Y",
			"50Y"
		};

		String[] astrGovvieTenor = new String[] {
			"1Y",
			"2Y",
			"3Y",
			"5Y",
			"7Y",
			"10Y",
			"20Y",
			"30Y"
		};

		double[] adblFixFloatQuote = new double[] {
			0.016410, //  2Y
			0.017863, //  3Y
			0.019030, //  4Y
			0.020035, //  5Y
			0.020902, //  6Y
			0.021660, //  7Y
			0.022307, //  8Y
			0.022879, //  9Y
			0.023363, // 10Y
			0.023820, // 11Y
			0.024172, // 12Y
			0.024934, // 15Y
			0.025581, // 20Y
			0.025906, // 25Y
			0.025973, // 30Y
			0.025838, // 40Y
			0.025560  // 50Y
		};

		double[] adblGovvieYield = new double[] {
			0.01219, //  1Y
			0.01391, //  2Y
			0.01590, //  3Y
			0.01937, //  5Y
			0.02200, //  7Y
			0.02378, // 10Y
			0.02677, // 20Y
			0.02927  // 30Y
		};

		String[] astrCreditTenor = new String[] {
			"06M",
			"01Y",
			"02Y",
			"03Y",
			"04Y",
			"05Y",
			"07Y",
			"10Y"
		};

		double[] adblCreditQuote = new double[] {
			 60.,	//  6M
			 68.,	//  1Y
			 88.,	//  2Y
			102.,	//  3Y
			121.,	//  4Y
			138.,	//  5Y
			168.,	//  7Y
			188.	// 10Y
		};

		double dblFX = 1.;
		int iSettleLag = 3;
		int iCouponFreq = 12;
		String strName = "Korba";
		double dblCleanPrice = 0.95;
		double dblIssuePrice = 1.;
		String strCurrency = "USD";
		double dblSpreadBump = 20.;
		double dblCouponRate = 0.03; 
		String strTreasuryCode = "UST";
		String strCouponDayCount = "30/360";
		double dblSpreadDurationMultiplier = 5.;

		org.drip.analytics.date.JulianDate[] adtPeriodEnd = new org.drip.analytics.date.JulianDate[] {
			DateUtil.CreateFromYMD (2017, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2017, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2017, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2018, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2018, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2019, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2019, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2020, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2020, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2021, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2021, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2022, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2022, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2023, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2023, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2024, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2024, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2025, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2025, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2026, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2026, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2027, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2027, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2028, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2028, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2029, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2029, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2030, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2030, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2031, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2031, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2032, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2032, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2033, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2033, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2034, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2034, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2035, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2035, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2036, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2036, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2037, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2037, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2038, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2038, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2039, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2039, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2040, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2040, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2041, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2041, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2042, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2042, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2043, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2043, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2044, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2044, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.MARCH    , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.APRIL    , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.MAY      , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.JUNE     , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.JULY     , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.AUGUST   , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.SEPTEMBER, 25),
			DateUtil.CreateFromYMD (2045, DateUtil.OCTOBER  , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.NOVEMBER , 25),
			DateUtil.CreateFromYMD (2045, DateUtil.DECEMBER , 25),
			DateUtil.CreateFromYMD (2046, DateUtil.JANUARY  , 25),
			DateUtil.CreateFromYMD (2046, DateUtil.FEBRUARY , 25),
			DateUtil.CreateFromYMD (2046, DateUtil.MARCH    , 25),
		};

		double[] adblPrincipalPayDown = new double[] {
			1042394.66,
			1022818.86,
			1004129.80,
			974934.20,
			957106.77,
			939707.76,
			922657.37,
			905884.73,
			889512.35,
			873677.83,
			864616.71,
			852795.67,
			837416.16,
			822345.44,
			807557.01,
			799363.65,
			784999.88,
			774438.91,
			760500.39,
			752641.94,
			742504.50,
			735131.64,
			721791.58,
			712205.44,
			704773.66,
			691885.60,
			684742.14,
			675528.64,
			668439.66,
			656462.04,
			652990.02,
			641304.10,
			634968.44,
			626832.46,
			620742.65,
			609695.73,
			607009.38,
			603903.35,
			596371.82,
			590476.70,
			584804.48,
			577509.36,
			571914.96,
			566346.07,
			559485.28,
			554040.80,
			548540.33,
			541657.01,
			536289.73,
			526630.27,
			521543.31,
			514933.57,
			509907.40,
			500859.44,
			495978.98,
			489841.69,
			485067.24,
			476454.32,
			471946.62,
			466072.58,
			461583.26,
			457226.26,
			452652.44,
			444720.08,
			442818.85,
			438397.53,
			434244.44,
			430065.06,
			422460.79,
			420612.69,
			416624.87,
			412543.43,
			405378.76,
			401446.60,
			397674.86,
			395916.46,
			392024.18,
			385216.45,
			381466.80,
			377887.02,
			374205.01,
			370631.36,
			366112.63,
			362673.96,
			359240.29,
			355743.84,
			352271.29,
			349018.01,
			343041.74,
			339733.29,
			336374.82,
			334939.82,
			331774.08,
			326190.16,
			323032.82,
			319911.89,
			316975.61,
			313909.57,
			308609.51,
			305713.96,
			304456.23,
			301443.00,
			298508.57,
			295551.65,
			290682.72,
			287833.49,
			285004.82,
			282264.53,
			279579.72,
			274938.37,
			272263.40,
			269673.48,
			268358.55,
			265809.43,
			263245.44,
			258858.82,
			256390.42,
			253980.67,
			251557.77,
			249111.72,
			246734.26,
			242754.14,
			240465.03,
			238152.45,
			235856.77,
			234804.12,
			232515.16,
			228776.49,
			226572.49,
			224514.41,
			222351.38,
			220261.58,
			218139.74,
			214724.59,
			212682.98,
			210713.08,
			208759.18,
			206853.37,
			204868.46,
			201531.66,
			199648.70,
			198731.52,
			196886.73,
			195019.08,
			193209.88,
			191438.82,
			188474.69,
			186712.44,
			185007.04,
			183379.35,
			181663.35,
			179977.88,
			178296.02,
			175694.27,
			174077.00,
			172521.03,
			170944.71,
			169432.01,
			167919.65,
			166384.87,
			164027.33,
			163210.74,
			161724.10,
			160296.41,
			158890.55,
			157461.36,
			156076.90,
			153846.28,
			152567.54,
			151188.13,
			149893.10,
			148615.29,
			147311.47,
			146032.75,
			144033.04,
			142843.78,
			141635.64,
			140439.62,
			139295.40,
			138168.91,
			136987.58,
			135895.20,
			134192.98,
			133090.15,
			132495.67,
			131478.32,
			130410.42,
			129434.94,
			128479.55,
			127482.61,
			126054.79,
			125216.37,
			124375.18,
			122850.27,
			121741.16,
			120855.76,
			120109.93,
			119397.88,
			118653.33,
			118020.88,
			117433.26,
			116833.62,
			116369.87,
			115991.65,
			115665.77,
			115486.44,
			113205.69,
			112388.04,
			112004.47,
			112201.48,
			112127.90,
			111539.05,
			113317.94,
			384849.62,
			288750.90,
			375798.57,
			320704.31,
			13849.71,
			13727.36,
			13606.05,
			13485.77,
			13366.52,
			13248.30,
			13131.08,
			13014.86,
			12899.64,
			12785.41,
			12672.16,
			12559.88,
			12448.56,
			12338.21,
			12228.80,
			12120.34,
			12012.81,
			11906.21,
			11800.54,
			11695.78,
			11591.93,
			11488.98,
			11386.92,
			11285.76,
			11185.48,
			11086.07,
			10987.53,
			10889.86,
			10793.04,
			10697.07,
			10601.95,
			10507.67,
			10414.22,
			10321.60,
			10229.80,
			10138.82,
			10048.65,
			9959.29,
			9870.73,
			9782.97,
			9695.99,
			9609.81,
			9524.40,
			9439.78,
			9355.93,
			9272.85,
			9190.53,
			9108.98,
			9028.19,
			8948.16,
			8868.88,
			8790.35,
			8712.58,
			8635.55,
			8559.27,
			8483.74,
			8408.97,
			8334.94,
			8261.66,
			8189.15,
			8117.40,
			8046.43,
			7976.24,
			7906.86,
			7838.30,
			7770.60,
			7703.79,
			7637.95,
			7573.17,
			7509.58,
			7447.43,
			15430.41,
			7217.42,
			7166.87,
			7064.29,
			6848.45,
			6788.98,
			6730.17,
			6672.01,
			6614.51,
			6557.67,
			6501.51,
			6446.02,
			6391.21,
			6337.10,
			6283.69,
			6230.99,
			6179.02,
			6127.79,
			6077.32,
			6027.64,
			5978.77,
			5930.74,
			5883.58,
			5837.33,
			5792.05,
			5747.80,
			5704.65,
			5662.69,
			5622.02,
			5582.81,
			5545.23,
			5509.53,
			5476.05,
			5445.31,
			5418.10,
			5395.78,
			5381.19,
			5382.69,
			5239.13,
			4921.48,
			4829.27,
			4735.30,
			4758.91,
			4467.33,
			4054.87,
			4039.73,
			3869.92,
			3753.53,
			13947.59,
			108760.73,
			0.00,
			0.00,
			0.00,
			18935930.73,
		};

		double[] adblCouponAmount = new double[] {
			214911.15,
			209801.35,
			208645.97,
			203949.72,
			198769.55,
			197754.66,
			192680.89,
			189056.23,
			184844.05,
			181872.94,
			176537.96,
			175565.07,
			171591.89,
			167172.78,
			165151.15,
			162047.63,
			158385.85,
			157056.72,
			152268.92,
			148763.77,
			149060.46,
			145612.12,
			139315.98,
			138099.47,
			135867.81,
			133187.13,
			131133.35,
			128714.53,
			127954.47,
			125933.77,
			123021.72,
			122133.79,
			119857.23,
			117716.89,
			116814.32,
			114413.23,
			112804.70,
			111352.48,
			109700.47,
			108911.53,
			106641.22,
			105758.51,
			103690.11,
			102402.43,
			100781.19,
			99373.12,
			98011.07,
			96666.90,
			95525.76,
			93935.33,
			92648.76,
			91350.19,
			90124.21,
			88857.19,
			87640.50,
			86412.38,
			85225.30,
			84054.91,
			82904.02,
			81742.19,
			80671.03,
			79536.51,
			78438.89,
			77331.19,
			76293.11,
			75212.00,
			74166.08,
			73156.98,
			72134.91,
			71111.01,
			70113.89,
			69107.90,
			68200.89,
			67224.69,
			66280.08,
			65327.04,
			64398.85,
			63502.14,
			62601.87,
			61693.82,
			60814.96,
			59965.50,
			59106.93,
			58241.71,
			57459.96,
			56614.50,
			55796.16,
			55004.46,
			54205.11,
			53404.76,
			52630.00,
			51880.13,
			51118.61,
			50381.25,
			49642.59,
			48898.09,
			48205.92,
			47478.83,
			46774.80,
			46097.30,
			45414.32,
			44721.83,
			44077.12,
			43401.05,
			42771.33,
			42115.56,
			41480.46,
			40864.45,
			40244.31,
			39642.59,
			39041.01,
			38457.17,
			37855.18,
			37260.28,
			36704.95,
			36124.23,
			35585.47,
			35022.14,
			34495.84,
			33945.86,
			33431.80,
			32894.94,
			32396.10,
			31875.20,
			31370.34,
			30867.35,
			30371.77,
			29889.85,
			29409.08,
			28941.41,
			28472.03,
			28015.31,
			27557.09,
			27100.33,
			26655.89,
			26223.22,
			25803.39,
			25366.63,
			24956.51,
			24530.22,
			24122.62,
			23709.23,
			23318.54,
			22912.85,
			22531.29,
			22135.35,
			21753.88,
			21369.77,
			21008.13,
			20644.17,
			20279.85,
			19916.26,
			19560.78,
			19213.88,
			18868.70,
			18531.73,
			18196.74,
			17858.17,
			17538.39,
			17207.85,
			16895.45,
			16576.14,
			16262.48,
			15955.98,
			15649.79,
			15344.07,
			15053.01,
			14753.00,
			14470.07,
			14172.60,
			13896.26,
			13618.69,
			13341.67,
			13065.02,
			12794.59,
			12531.03,
			12269.03,
			12005.29,
			11754.32,
			11502.50,
			11246.58,
			11000.57,
			10760.62,
			10511.15,
			10277.63,
			10042.41,
			9803.74,
			9573.79,
			9349.22,
			9115.61,
			8895.89,
			8676.39,
			8453.94,
			8238.91,
			8029.57,
			7813.00,
			7608.49,
			7403.70,
			7196.49,
			6995.67,
			6798.91,
			6595.69,
			6402.70,
			6209.62,
			6014.35,
			5824.16,
			5634.41,
			5445.40,
			5262.51,
			5076.74,
			4895.50,
			4708.98,
			4529.63,
			4347.02,
			3658.90,
			3092.03,
			2415.05,
			2128.00,
			2101.10,
			2074.44,
			2048.03,
			2021.87,
			1995.94,
			1970.26,
			1944.81,
			1919.60,
			1894.62,
			1869.88,
			1845.36,
			1821.07,
			1797.01,
			1773.17,
			1749.55,
			1726.15,
			1702.96,
			1680.00,
			1657.24,
			1634.70,
			1612.37,
			1590.24,
			1568.32,
			1546.60,
			1525.09,
			1503.77,
			1482.66,
			1461.74,
			1441.01,
			1420.47,
			1400.13,
			1379.97,
			1360.00,
			1340.22,
			1320.66,
			1301.32,
			1282.15,
			1263.16,
			1244.35,
			1225.71,
			1207.24,
			1188.94,
			1170.88,
			1152.99,
			1135.27,
			1117.72,
			1100.32,
			1083.09,
			1066.02,
			1049.11,
			1032.35,
			1015.75,
			999.31,
			983.01,
			966.87,
			950.87,
			935.03,
			919.32,
			903.76,
			888.35,
			873.08,
			857.95,
			842.96,
			828.10,
			813.38,
			798.78,
			784.31,
			769.98,
			755.83,
			741.83,
			727.97,
			718.25,
			704.58,
			691.02,
			677.59,
			664.49,
			651.52,
			638.68,
			625.96,
			613.34,
			600.86,
			588.51,
			576.26,
			564.11,
			552.06,
			540.14,
			528.35,
			516.65,
			505.03,
			493.63,
			482.33,
			471.13,
			460.01,
			448.98,
			438.03,
			427.17,
			416.38,
			405.68,
			395.04,
			384.47,
			373.97,
			363.53,
			353.15,
			342.82,
			332.52,
			322.28,
			312.07,
			301.50,
			291.57,
			281.91,
			272.74,
			263.63,
			254.66,
			245.65,
			236.98,
			228.83,
			220.65,
			207.80,
			183.35,
			175.62,
			0.00,
			0.00,
			0.00,
			0.00,
		};

		double dblIssueAmount = Matrix.Sum (adblPrincipalPayDown);

		JulianDate dtEffective = DateUtil.CreateFromYMD (
			2017,
			DateUtil.SEPTEMBER,
			25
		);

		BondComponent bond = BondBuilder.CreateBondFromCF (
			strName,
			dtEffective,
			strCurrency,
			strName,
			strCouponDayCount,
			dblIssueAmount,
			dblCouponRate,
			iCouponFreq,
			adtPeriodEnd,
			adblCouponAmount,
			adblPrincipalPayDown,
			true
		);

		BondReplicator abr = BondReplicator.Standard (
			dblCleanPrice,
			dblIssuePrice,
			dblIssueAmount,
			dtSpot,
			astrDepositTenor,
			adblDepositQuote,
			adblFuturesQuote,
			astrFixFloatTenor,
			adblFixFloatQuote,
			dblSpreadBump,
			dblSpreadDurationMultiplier,
			strTreasuryCode,
			astrGovvieTenor,
			adblGovvieYield,
			astrCreditTenor,
			adblCreditQuote,
			dblFX,
			Double.NaN,
			iSettleLag,
			bond
		);

		BondReplicationRun abrr = abr.generateRun();

		System.out.println (abrr.display());

		double dblBalance = 1.;

		for (CompositePeriod p : bond.couponPeriods()) {
			int iEndDate = p.endDate();

			int iStartDate = p.startDate();

			double dblPrincipalPayDown = bond.notional (iStartDate) - bond.notional (iEndDate);

			double dblInterest = dblCouponRate * p.couponDCF() * bond.notional (iStartDate) * bond.couponFactor (iEndDate);

			dblBalance -= dblPrincipalPayDown;

			System.out.println (
				"\t" + new JulianDate (iEndDate) + " => " +
				FormatUtil.FormatDouble (dblPrincipalPayDown, 8, 2, dblIssueAmount) + " | " +
				FormatUtil.FormatDouble (dblInterest, 6, 2, dblIssueAmount) + " | " +
				FormatUtil.FormatDouble (dblPrincipalPayDown + dblInterest, 8, 2, dblIssueAmount) + " | " +
				FormatUtil.FormatDouble (dblBalance, 8, 2, dblIssueAmount) + " ||"
			);
		}
	}
}
