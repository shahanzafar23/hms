import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PatientMedicalRecordService } from '../service/patient-medical-record.service';
import { IPatientMedicalRecord, PatientMedicalRecord } from '../patient-medical-record.model';

import { PatientMedicalRecordUpdateComponent } from './patient-medical-record-update.component';

describe('PatientMedicalRecord Management Update Component', () => {
  let comp: PatientMedicalRecordUpdateComponent;
  let fixture: ComponentFixture<PatientMedicalRecordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patientMedicalRecordService: PatientMedicalRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PatientMedicalRecordUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PatientMedicalRecordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatientMedicalRecordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patientMedicalRecordService = TestBed.inject(PatientMedicalRecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const patientMedicalRecord: IPatientMedicalRecord = { id: 'CBA' };

      activatedRoute.data = of({ patientMedicalRecord });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(patientMedicalRecord));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientMedicalRecord>>();
      const patientMedicalRecord = { id: 'ABC' };
      jest.spyOn(patientMedicalRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientMedicalRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patientMedicalRecord }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(patientMedicalRecordService.update).toHaveBeenCalledWith(patientMedicalRecord);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientMedicalRecord>>();
      const patientMedicalRecord = new PatientMedicalRecord();
      jest.spyOn(patientMedicalRecordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientMedicalRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patientMedicalRecord }));
      saveSubject.complete();

      // THEN
      expect(patientMedicalRecordService.create).toHaveBeenCalledWith(patientMedicalRecord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatientMedicalRecord>>();
      const patientMedicalRecord = { id: 'ABC' };
      jest.spyOn(patientMedicalRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patientMedicalRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patientMedicalRecordService.update).toHaveBeenCalledWith(patientMedicalRecord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
