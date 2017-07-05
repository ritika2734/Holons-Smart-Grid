package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.HolonObjectType;
import com.htc.service.DaoAware;
import com.htc.service.HolonObjectTypeService;

/**
 * This class is the implementation of interface HolonObjectTypeService and calls respective functions of the next DAO layer.
 *
 */
public class HolonObjectTypeServiceImpl extends DaoAware implements HolonObjectTypeService {

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectTypeService#persist(com.htc.hibernate.pojo.HolonObjectType)
	 */
	@Override
	public Integer persist(HolonObjectType transientInstance) {
		return getHolonObjectTypeDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectTypeService#merge(com.htc.hibernate.pojo.HolonObjectType)
	 */
	@Override
	public HolonObjectType merge(HolonObjectType detachedInstance) {
		return getHolonObjectTypeDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectTypeService#findById(int)
	 */
	@Override
	public HolonObjectType findById(int holonObjectTypeId) {
		return getHolonObjectTypeDao().findById(holonObjectTypeId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectTypeService#delete(com.htc.hibernate.pojo.HolonObjectType)
	 */
	@Override
	public boolean delete(HolonObjectType persistentInstance) {
		return getHolonObjectTypeDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectTypeService#getAllHolonObjectType()
	 */
	@Override
	public ArrayList<HolonObjectType> getAllHolonObjectType() {
		return getHolonObjectTypeDao().getAllHolonObjectType();
	}

}
