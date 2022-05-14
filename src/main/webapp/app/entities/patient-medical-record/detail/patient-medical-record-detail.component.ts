import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatientMedicalRecord } from '../patient-medical-record.model';

@Component({
  selector: 'jhi-patient-medical-record-detail',
  templateUrl: './patient-medical-record-detail.component.html',
})
export class PatientMedicalRecordDetailComponent implements OnInit {
  patientMedicalRecord: IPatientMedicalRecord | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patientMedicalRecord }) => {
      this.patientMedicalRecord = patientMedicalRecord;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
