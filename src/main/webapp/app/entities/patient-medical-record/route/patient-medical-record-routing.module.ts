import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PatientMedicalRecordComponent } from '../list/patient-medical-record.component';
import { PatientMedicalRecordDetailComponent } from '../detail/patient-medical-record-detail.component';
import { PatientMedicalRecordUpdateComponent } from '../update/patient-medical-record-update.component';
import { PatientMedicalRecordRoutingResolveService } from './patient-medical-record-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const patientMedicalRecordRoute: Routes = [
  {
    path: '',
    component: PatientMedicalRecordComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatientMedicalRecordDetailComponent,
    resolve: {
      patientMedicalRecord: PatientMedicalRecordRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':patientId/new',
    component: PatientMedicalRecordUpdateComponent,
    resolve: {
      patientMedicalRecord: PatientMedicalRecordRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.DOCTOR, Authority.INSURER, Authority.ER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatientMedicalRecordUpdateComponent,
    resolve: {
      patientMedicalRecord: PatientMedicalRecordRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(patientMedicalRecordRoute)],
  exports: [RouterModule],
})
export class PatientMedicalRecordRoutingModule {}
