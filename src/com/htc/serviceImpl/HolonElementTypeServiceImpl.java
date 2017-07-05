package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.HolonElementType;
import com.htc.service.DaoAware;
import com.htc.service.HolonElementTypeService;

/**
 * This class is the implementation of interface HolonElementTypeService and calls respective functions of the next DAO layer.
 *
 */
public class HolonElementTypeServiceImpl extends DaoAware implements HolonElementTypeService {

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementTypeService#persist(com.htc.hibernate.pojo.HolonElementType)
	 */
	@Override
	public Integer persist(HolonElementType transientInstance) {
		return getHolonElementTypeDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementTypeService#merge(com.htc.hibernate.pojo.HolonElementType)
	 */
	@Override
	public HolonElementType merge(HolonElementType detachedInstance) {
		return getHolonElementTypeDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementTypeService#findById(int)
	 */
	@Override
	public HolonElementType findById(int holonElementTypeId) {
		return getHolonElementTypeDao().findById(holonElementTypeId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementTypeService#delete(com.htc.hibernate.pojo.HolonElementType)
	 */
	@Override
	public boolean delete(HolonElementType persistentInstance) {
		return getHolonElementTypeDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementTypeService#getAllHolonElementType()
	 */
	@Override
	public ArrayList<HolonElementType> getAllHolonElementType() {
		return getHolonElementTypeDao().getAllHolonElementType();
	}

}
