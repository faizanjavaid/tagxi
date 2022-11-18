<?php

namespace App\Http\Controllers\Web\Admin;

use App\Base\Constants\Taxi\DriverDocumentStatus;
use App\Base\Services\ImageUploader\ImageUploaderContract;
use App\Http\Controllers\Controller;
use App\Http\Controllers\Web\BaseController;
use App\Http\Requests\Taxi\Driver\DriverDocumentUploadRequest;
use App\Models\Admin\Owner;
use App\Models\Admin\OwnerDocument;
use App\Models\Admin\OwnerNeededDocument;
use App\Models\User;
use Illuminate\Http\Request;

class OwnerDocumentController extends BaseController
{
    
    /**
     * The
     *
     * @var App\Base\Services\ImageUploader\ImageUploaderContract
     */
    protected $imageUploader;

    /**
     * DriverController constructor.
     *
     * @param \App\Models\Admin\Driver $driver
     */
    public function __construct(ImageUploaderContract $imageUploader)
    {
        $this->imageUploader = $imageUploader;
    }

    public function index(Owner $owner)
    {
        $neededDocument = OwnerNeededDocument::whereActive(true)->get();
        $ownerDoc = OwnerDocument::whereOwnerId($owner->id)->get();

        $page = trans('pages_names.owner_document');
        $main_menu = 'manage_owners';
        $sub_menu = $owner->area->name;

        return view('admin.owners.documents.index', compact('page', 'main_menu', 'sub_menu', 'owner', 'neededDocument', 'ownerDoc'));
    }

    public function documentUploadView(Owner $owner, OwnerNeededDocument $needed_document)
    {
        $ownerDoc = null;
        if ($needed_document->ownerDocument) {
            $ownerDoc = $needed_document->ownerDocument->where('owner_id', $owner->id)->whereDocumentId($needed_document->id)->first();
        }

        $page = trans('pages_names.owner_document');
        $main_menu = 'manage_owners';
        $sub_menu = $owner->area->name;

        return view('taxi.owners.documents.upload', compact('page', 'main_menu', 'sub_menu', 'owner', 'needed_document', 'ownerDoc'));
    }


    public function uploadDocument(DriverDocumentUploadRequest $request, Owner $owner, OwnerNeededDocument $needed_document)
    {
        $expiry_date = $request->expiry_date;
        $this->uploadOwnerDoc('document',$expiry_date,$request,$owner,$needed_document);
     
        $message = trans('success_messages.owner_document_uploaded_successfully');

        return redirect("owners/document/view/$owner->id")->with('success', $message);
    }


    public function approveOwnerDocument(Request $request)
    {
        $status = true;

        $owner = Owner::find($request->owner_id);
        foreach ($request->document_id as $key => $document) {
            if ($document != '') {
                $ownerDoc = OwnerDocument::whereId($document)->first();

                $ownerDoc->document_status = $request->document_status[$key];
                $ownerDoc->comment = $request->comment[$key];
                $ownerDoc->save();

                if ($ownerDoc->document_status != 1) {
                    $status = false;
                }
            } else {
                $status = false;
            }
        }

        if(!$owner->user->email_confirmed){
            $status = false;
        }

        $status = $status == true ? 1 : 0;
        $owner->update([
            'approve' => $status
        ]);

        return 'success';
    }

    public function uploadOwnerDoc($name,$expiry_date = null,Request $request,Owner $owner, OwnerNeededDocument $needed_document){
        // dd($needed_document->id);
        $created_params['expiry_date'] = $expiry_date;

        $created_params['owner_id'] = $owner->id;
        $created_params['document_id'] = $needed_document->id;

        if ($uploadedFile = $this->getValidatedUpload($name, $request)) {
            $created_params['image'] = $this->imageUploader->file($uploadedFile)
                ->saveOwnerDocument($owner->id);
        }

        // Check if document exists
        $owner_documents = OwnerDocument::where('owner_id', $owner->id)->where('document_id', $needed_document->id)->first();

        if ($owner_documents) {
            $created_params['document_status'] = DriverDocumentStatus::REUPLOADED_AND_WAITING_FOR_APPROVAL;
            OwnerDocument::where('owner_id', $owner->id)->where('document_id', $needed_document->id)->update($created_params);
        } else {
            $created_params['document_status'] = DriverDocumentStatus::UPLOADED_AND_WAITING_FOR_APPROVAL;
            OwnerDocument::create($created_params);
        }
    }
}
