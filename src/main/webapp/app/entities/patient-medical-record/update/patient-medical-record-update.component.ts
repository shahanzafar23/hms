import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPatientMedicalRecord, PatientMedicalRecord } from '../patient-medical-record.model';
import { PatientMedicalRecordService } from '../service/patient-medical-record.service';

@Component({
  selector: 'jhi-patient-medical-record-update',
  templateUrl: './patient-medical-record-update.component.html',
})
export class PatientMedicalRecordUpdateComponent implements OnInit {
  isSaving = false;
  walletId: string | undefined;

  editForm = this.fb.group({
    id: [],
    patientCondition: [],
    insuranceClaimed: [],
  });

  constructor(
    protected patientMedicalRecordService: PatientMedicalRecordService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patientMedicalRecord }) => {
      this.updateForm(patientMedicalRecord);
    });
    this.activatedRoute.params.subscribe(params => {
      this.walletId = params['patientId'];
    })
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patientMedicalRecord = this.createFromForm();
    if (patientMedicalRecord.id !== undefined) {
      this.subscribeToSaveResponse(this.patientMedicalRecordService.update(patientMedicalRecord));
    } else if(this.walletId) {
      this.subscribeToSaveResponse(this.patientMedicalRecordService.create(patientMedicalRecord, this.walletId));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatientMedicalRecord>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(patientMedicalRecord: IPatientMedicalRecord): void {
    this.editForm.patchValue({
      id: patientMedicalRecord.id,
      patientCondition: patientMedicalRecord.patientCondition,
      insuranceClaimed: patientMedicalRecord.insuranceClaimed,
    });
  }

  protected createFromForm(): IPatientMedicalRecord {
    return {
      ...new PatientMedicalRecord(),
      id: this.editForm.get(['id'])!.value,
      patientCondition: this.editForm.get(['patientCondition'])!.value,
      insuranceClaimed: this.editForm.get(['insuranceClaimed'])!.value,
    };
  }
}
