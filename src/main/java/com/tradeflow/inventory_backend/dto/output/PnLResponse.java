package com.tradeflow.inventory_backend.dto.output;

import com.tradeflow.inventory_backend.dto.PnLData;

import java.util.List;

public class PnLResponse {
	private List<PnLData> periods;
	private PnLData summary;

	public PnLResponse(List<PnLData> periods, PnLData summary) {
		this.periods = periods;
		this.summary = summary;
	}

	// Getters and setters
	public List<PnLData> getPeriods() { return periods; }
	public void setPeriods(List<PnLData> periods) { this.periods = periods; }

	public PnLData getSummary() { return summary; }
	public void setSummary(PnLData summary) { this.summary = summary; }
}