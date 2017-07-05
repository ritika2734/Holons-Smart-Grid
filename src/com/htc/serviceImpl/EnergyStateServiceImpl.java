package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.EnergyState;
import com.htc.service.DaoAware;
import com.htc.service.EnergyStateService;

/**
 * This class is the implementation of interface EnergyStateService and calls respective functions of the next DAO layer.
 */
public class EnergyStateServiceImpl extends DaoAware  implements EnergyStateService {

	/* (non-Javadoc)
	 * @see com.htc.service.EnergyStateService#findById(int)
	 */
	@Override
	public EnergyState findById(int energyStateId) {
		return getEnergyStateDao().findById(energyStateId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.EnergyStateService#getAllEnergyState()
	 */
	@Override
	public ArrayList<EnergyState> getAllEnergyState() {
		return getEnergyStateDao().getAllEnergyState();
	}

}
