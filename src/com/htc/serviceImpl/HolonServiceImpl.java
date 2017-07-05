package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.Holon;
import com.htc.service.DaoAware;
import com.htc.service.HolonService;

/**
 * This class is the implementation of interface HolonService and calls respective functions of the next DAO layer.
 *
 */
public class HolonServiceImpl extends DaoAware implements HolonService {

	/* (non-Javadoc)
	 * @see com.htc.service.HolonService#persist(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public Integer persist(Holon transientInstance) {
		return getHolonDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonService#merge(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public Holon merge(Holon detachedInstance) {
		return getHolonDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonService#findById(int)
	 */
	@Override
	public Holon findById(int holonId) {
		return getHolonDao().findById(holonId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonService#delete(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public boolean delete(Holon persistentInstance) {
		return getHolonDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonService#getAllHolon()
	 */
	@Override
	public ArrayList<Holon> getAllHolon() {
		return getHolonDao().getAllHolon();
	}

}
