package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.service.DaoAware;
import com.htc.service.HolonElementService;

/**
 * This class is the implementation of interface HolonElementService and calls respective functions of the next DAO layer.
 */
public class HolonElementServiceImpl extends DaoAware implements HolonElementService {

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementService#persist(com.htc.hibernate.pojo.HolonElement)
	 */
	@Override
	public Integer persist(HolonElement transientInstance) {
		return getHolonElementDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementService#merge(com.htc.hibernate.pojo.HolonElement)
	 */
	@Override
	public HolonElement merge(HolonElement detachedInstance) {
		return getHolonElementDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementService#findById(int)
	 */
	@Override
	public HolonElement findById(int holonElementId) {
		return getHolonElementDao().findById(holonElementId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementService#delete(com.htc.hibernate.pojo.HolonElement)
	 */
	@Override
	public boolean delete(HolonElement persistentInstance) {
		return getHolonElementDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementService#getAllHolonElement()
	 */
	@Override
	public ArrayList<HolonElement> getAllHolonElement() {
		return getHolonElementDao().getAllHolonElement();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementService#getHolonElements(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<HolonElement> getHolonElements(HolonObject holonObject) {
		return getHolonElementDao().getHolonElements(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonElementService#deleteAllHolonElements()
	 */
	@Override
	public int deleteAllHolonElements() {
		return getHolonElementDao().deleteAllHolonElements();
	}

}
