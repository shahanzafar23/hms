import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPatientMedicalRecord } from '../patient-medical-record.model';
import { PatientMedicalRecordService } from '../service/patient-medical-record.service';

@Component({
  templateUrl: './patient-medical-record-delete-dialog.component.html',
})
export class PatientMedicalRecordDeleteDialogComponent {
  patientMedicalRecord?: IPatientMedicalRecord;

  constructor(protected patientMedicalRecordService: PatientMedicalRecordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.patientMedicalRecordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
