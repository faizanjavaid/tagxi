<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePocClientsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('poc_clients', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->unsignedInteger('project_id');
            $table->unsignedInteger('user_id');
            $table->timestamps();


            $table->foreign('user_id')
                 ->references('id')
                    ->on('users')
                    ->onDelete('cascade');

            $table->foreign('project_id')
                 ->references('id')
                    ->on('projects')
                    ->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('poc_clients');
    }
}
