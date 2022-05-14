import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPatientMedicalRecord, PatientMedicalRecord } from '../patient-medical-record.model';
import { PatientMedicalRecordService } from '../service/patient-medical-record.service';

import { PatientMedicalRecordRoutingResolveService } from './patient-medical-record-routing-resolve.service';

describe('PatientMedicalRecord routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PatientMedicalRecordRoutingResolveService;
  let service: PatientMedicalRecordService;
  let resultPatientMedicalRecord: IPatientMedicalRecord | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PatientMedicalRecordRoutingResolveService);
    service = TestBed.inject(PatientMedicalRecordService);
    resultPatientMedicalRecord = undefined;
  });

  describe('resolve', () => {
    it('should return IPatientMedicalRecord returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientMedicalRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultPatientMedicalRecord).toEqual({ id: 'ABC' });
    });

    it('should return new IPatientMedicalRecord if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientMedicalRecord = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPatientMedicalRecord).toEqual(new PatientMedicalRecord());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PatientMedicalRecord })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPatientMedicalRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultPatientMedicalRecord).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
