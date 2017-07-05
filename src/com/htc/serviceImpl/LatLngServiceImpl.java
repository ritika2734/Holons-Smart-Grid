package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.LatLng;
import com.htc.service.DaoAware;
import com.htc.service.LatLngService;

/**
 * This class is the implementation of interface LatLngService and calls respective functions of the next DAO layer.
 *
 */
public class LatLngServiceImpl extends DaoAware implements LatLngService {

	/* (non-Javadoc)
	 * @see com.htc.service.LatLngService#persist(com.htc.hibernate.pojo.LatLng)
	 */
	@Override
	public Integer persist(LatLng transientInstance) {
		return getLatLngDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.LatLngService#merge(com.htc.hibernate.pojo.LatLng)
	 */
	@Override
	public LatLng merge(LatLng detachedInstance) {
		return getLatLngDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.LatLngService#findById(int)
	 */
	@Override
	public LatLng findById(int latLngId) {
		return getLatLngDao().findById(latLngId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.LatLngService#delete(com.htc.hibernate.pojo.LatLng)
	 */
	@Override
	public boolean delete(LatLng persistentInstance) {
		return getLatLngDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.LatLngService#getAllLatLng()
	 */
	@Override
	public ArrayList<LatLng> getAllLatLng() {
		return getLatLngDao().getAllLatLng();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.LatLngService#findByLocation(java.lang.Double, java.lang.Double)
	 */
	@Override
	public ArrayList<LatLng> findByLocation(Double lat, Double lng) {
		return getLatLngDao().findByLocation(lat,lng);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.LatLngService#findAllLatLngInsideTheCircle(java.lang.Double, java.lang.Double, java.lang.Double)
	 */
	@Override
	public ArrayList<LatLng> findAllLatLngInsideTheCircle(Double lat, Double lng, Double radius) {
		return getLatLngDao().findAllLatLngInsideTheCircle(lat,lng,radius);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.LatLngService#deleteAllLatLngs()
	 */
	@Override
	public int deleteAllLatLngs() {
		return getLatLngDao().deleteAllLatLngs();
	}

}
