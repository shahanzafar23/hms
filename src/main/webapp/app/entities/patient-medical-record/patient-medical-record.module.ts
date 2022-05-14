import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PatientMedicalRecordComponent } from './list/patient-medical-record.component';
import { PatientMedicalRecordDetailComponent } from './detail/patient-medical-record-detail.component';
import { PatientMedicalRecordUpdateComponent } from './update/patient-medical-record-update.component';
import { PatientMedicalRecordDeleteDialogComponent } from './delete/patient-medical-record-delete-dialog.component';
import { PatientMedicalRecordRoutingModule } from './route/patient-medical-record-routing.module';

@NgModule({
  imports: [SharedModule, PatientMedicalRecordRoutingModule],
  declarations: [
    PatientMedicalRecordComponent,
    PatientMedicalRecordDetailComponent,
    PatientMedicalRecordUpdateComponent,
    PatientMedicalRecordDeleteDialogComponent,
  ],
  entryComponents: [PatientMedicalRecordDeleteDialogComponent],
})
export class PatientMedicalRecordModule {}
