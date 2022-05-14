import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPatientMedicalRecord, getPatientMedicalRecordIdentifier } from '../patient-medical-record.model';
import { IPatientMedicalHistory } from '../patient-medical-history.model';

export type EntityResponseType = HttpResponse<IPatientMedicalRecord>;
export type EntityArrayResponseType = HttpResponse<IPatientMedicalRecord[]>;

@Injectable({ providedIn: 'root' })
export class PatientMedicalRecordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/patient-medical-records');
  protected resourceUrlAll = this.applicationConfigService.getEndpointFor('api/patient-medical-records/all');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(patientMedicalRecord: IPatientMedicalRecord, id: string): Observable<EntityResponseType> {
    return this.http.post<IPatientMedicalRecord>(`${this.resourceUrl}/${id}`, patientMedicalRecord, { observe: 'response' });
  }

  update(patientMedicalRecord: IPatientMedicalRecord): Observable<EntityResponseType> {
    return this.http.put<IPatientMedicalRecord>(
      `${this.resourceUrl}/${getPatientMedicalRecordIdentifier(patientMedicalRecord) as string}`,
      patientMedicalRecord,
      { observe: 'response' }
    );
  }

  partialUpdate(patientMedicalRecord: IPatientMedicalRecord): Observable<EntityResponseType> {
    return this.http.patch<IPatientMedicalRecord>(
      `${this.resourceUrl}/${getPatientMedicalRecordIdentifier(patientMedicalRecord) as string}`,
      patientMedicalRecord,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPatientMedicalRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPatientMedicalRecord[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryForAll(id: string, req?: any): Observable<HttpResponse<IPatientMedicalHistory[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPatientMedicalHistory[]>(`${this.resourceUrlAll}/${id}`, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPatientMedicalRecordToCollectionIfMissing(
    patientMedicalRecordCollection: IPatientMedicalRecord[],
    ...patientMedicalRecordsToCheck: (IPatientMedicalRecord | null | undefined)[]
  ): IPatientMedicalRecord[] {
    const patientMedicalRecords: IPatientMedicalRecord[] = patientMedicalRecordsToCheck.filter(isPresent);
    if (patientMedicalRecords.length > 0) {
      const patientMedicalRecordCollectionIdentifiers = patientMedicalRecordCollection.map(
        patientMedicalRecordItem => getPatientMedicalRecordIdentifier(patientMedicalRecordItem)!
      );
      const patientMedicalRecordsToAdd = patientMedicalRecords.filter(patientMedicalRecordItem => {
        const patientMedicalRecordIdentifier = getPatientMedicalRecordIdentifier(patientMedicalRecordItem);
        if (patientMedicalRecordIdentifier == null || patientMedicalRecordCollectionIdentifiers.includes(patientMedicalRecordIdentifier)) {
          return false;
        }
        patientMedicalRecordCollectionIdentifiers.push(patientMedicalRecordIdentifier);
        return true;
      });
      return [...patientMedicalRecordsToAdd, ...patientMedicalRecordCollection];
    }
    return patientMedicalRecordCollection;
  }
}
