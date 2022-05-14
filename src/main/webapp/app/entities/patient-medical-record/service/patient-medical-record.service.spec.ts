import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPatientMedicalRecord, PatientMedicalRecord } from '../patient-medical-record.model';

import { PatientMedicalRecordService } from './patient-medical-record.service';

describe('PatientMedicalRecord Service', () => {
  let service: PatientMedicalRecordService;
  let httpMock: HttpTestingController;
  let elemDefault: IPatientMedicalRecord;
  let expectedResult: IPatientMedicalRecord | IPatientMedicalRecord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PatientMedicalRecordService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      patientCondition: 'AAAAAAA',
      insuranceClaimed: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PatientMedicalRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PatientMedicalRecord()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PatientMedicalRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          patientCondition: 'BBBBBB',
          insuranceClaimed: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PatientMedicalRecord', () => {
      const patchObject = Object.assign(
        {
          insuranceClaimed: true,
        },
        new PatientMedicalRecord()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PatientMedicalRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          patientCondition: 'BBBBBB',
          insuranceClaimed: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PatientMedicalRecord', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPatientMedicalRecordToCollectionIfMissing', () => {
      it('should add a PatientMedicalRecord to an empty array', () => {
        const patientMedicalRecord: IPatientMedicalRecord = { id: 'ABC' };
        expectedResult = service.addPatientMedicalRecordToCollectionIfMissing([], patientMedicalRecord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patientMedicalRecord);
      });

      it('should not add a PatientMedicalRecord to an array that contains it', () => {
        const patientMedicalRecord: IPatientMedicalRecord = { id: 'ABC' };
        const patientMedicalRecordCollection: IPatientMedicalRecord[] = [
          {
            ...patientMedicalRecord,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addPatientMedicalRecordToCollectionIfMissing(patientMedicalRecordCollection, patientMedicalRecord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PatientMedicalRecord to an array that doesn't contain it", () => {
        const patientMedicalRecord: IPatientMedicalRecord = { id: 'ABC' };
        const patientMedicalRecordCollection: IPatientMedicalRecord[] = [{ id: 'CBA' }];
        expectedResult = service.addPatientMedicalRecordToCollectionIfMissing(patientMedicalRecordCollection, patientMedicalRecord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patientMedicalRecord);
      });

      it('should add only unique PatientMedicalRecord to an array', () => {
        const patientMedicalRecordArray: IPatientMedicalRecord[] = [
          { id: 'ABC' },
          { id: 'CBA' },
          { id: 'd0c19ebc-bc1b-41cc-99a4-0d74f41e5975' },
        ];
        const patientMedicalRecordCollection: IPatientMedicalRecord[] = [{ id: 'ABC' }];
        expectedResult = service.addPatientMedicalRecordToCollectionIfMissing(patientMedicalRecordCollection, ...patientMedicalRecordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const patientMedicalRecord: IPatientMedicalRecord = { id: 'ABC' };
        const patientMedicalRecord2: IPatientMedicalRecord = { id: 'CBA' };
        expectedResult = service.addPatientMedicalRecordToCollectionIfMissing([], patientMedicalRecord, patientMedicalRecord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patientMedicalRecord);
        expect(expectedResult).toContain(patientMedicalRecord2);
      });

      it('should accept null and undefined values', () => {
        const patientMedicalRecord: IPatientMedicalRecord = { id: 'ABC' };
        expectedResult = service.addPatientMedicalRecordToCollectionIfMissing([], null, patientMedicalRecord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patientMedicalRecord);
      });

      it('should return initial array if no PatientMedicalRecord is added', () => {
        const patientMedicalRecordCollection: IPatientMedicalRecord[] = [{ id: 'ABC' }];
        expectedResult = service.addPatientMedicalRecordToCollectionIfMissing(patientMedicalRecordCollection, undefined, null);
        expect(expectedResult).toEqual(patientMedicalRecordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
