package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.HolonElementState;
import com.htc.service.DaoAware;
import com.htc.service.HolonElementStateService;

/**
 * This class is the implementation of interface HolonElementStateService and calls respective functions of the next DAO layer.
 *
 */
public class HolonElementStateServiceImpl extends DaoAware implements HolonElementStateService {

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementStateService#persist(com.htc.hibernate.pojo.HolonElementState)
	 */
	@Override
	public Integer persist(HolonElementState transientInstance) {
		return getHolonElementStateDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementStateService#merge(com.htc.hibernate.pojo.HolonElementState)
	 */
	@Override
	public HolonElementState merge(HolonElementState detachedInstance) {
		return getHolonElementStateDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementStateService#findById(int)
	 */
	@Override
	public HolonElementState findById(int holonElementStateId) {
		return getHolonElementStateDao().findById(holonElementStateId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementStateService#delete(com.htc.hibernate.pojo.HolonElementState)
	 */
	@Override
	public boolean delete(HolonElementState persistentInstance) {
		return getHolonElementStateDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementStateService#getAllHolonElementState()
	 */
	@Override
	public ArrayList<HolonElementState> getAllHolonElementState() {
		return getHolonElementStateDao().getAllHolonElementState();
	}

}
