import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPatientMedicalRecord, PatientMedicalRecord } from '../patient-medical-record.model';
import { PatientMedicalRecordService } from '../service/patient-medical-record.service';

@Injectable({ providedIn: 'root' })
export class PatientMedicalRecordRoutingResolveService implements Resolve<IPatientMedicalRecord> {
  constructor(protected service: PatientMedicalRecordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPatientMedicalRecord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((patientMedicalRecord: HttpResponse<PatientMedicalRecord>) => {
          if (patientMedicalRecord.body) {
            return of(patientMedicalRecord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PatientMedicalRecord());
  }
}
