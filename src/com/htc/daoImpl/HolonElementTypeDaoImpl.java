package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.HolonElementTypeDao;
import com.htc.hibernate.pojo.HolonElementType;
import com.htc.hibernate.utilities.HolonElementTypeHome;

/**
 * This class is the implementation of HolonElementTypeDao and calls respective functions of the next layer.
 */
public class HolonElementTypeDaoImpl implements HolonElementTypeDao {
	
	HolonElementTypeHome holonElementTypeHome = new HolonElementTypeHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementTypeDao#persist(com.htc.hibernate.pojo.HolonElementType)
	 */
	@Override
	public Integer persist(HolonElementType transientInstance) {
		return holonElementTypeHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementTypeDao#merge(com.htc.hibernate.pojo.HolonElementType)
	 */
	@Override
	public HolonElementType merge(HolonElementType detachedInstance) {
		return holonElementTypeHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementTypeDao#findById(int)
	 */
	@Override
	public HolonElementType findById(int holonElementTypeId) {
		return holonElementTypeHome.findById(holonElementTypeId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementTypeDao#delete(com.htc.hibernate.pojo.HolonElementType)
	 */
	@Override
	public boolean delete(HolonElementType persistentInstance) {
		return holonElementTypeHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementTypeDao#getAllHolonElementType()
	 */
	@Override
	public ArrayList<HolonElementType> getAllHolonElementType() {
		return holonElementTypeHome.getAllHolonElementType();
	}

}
